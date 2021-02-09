package me.hoy.demoinfleranrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hoy.demoinfleranrestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// slice test
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;
    // 단위 테스트는 아니다. 웹서버는 띄우진 않지만. 단위테스트보다는 느리다

    @Autowired
    ObjectMapper objectMapper;


    @Autowired
    EventRepository eventRepository;
    @Test
    @TestDescription("정상적으로 생성")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Developement")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,11,11,11,11,11))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,11,11,11,11,11))
                .beginEventDateTime(LocalDateTime.of(2020,11,11,11,11,11))
                .endEventDateTime(LocalDateTime.of(2020,11,11,11,11,11))
                .basePrice(100)
                .maxPrice(200)
                .limitOffEnrollment(100)
                .location("강남역")
                .build();


        mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists()) //이벤트들
                .andExpect(jsonPath("_links.update-event").exists()) //업데이트하는 링크
        ;
    }
    @Test
    @TestDescription("정장적인 값으로 요청받지 못했을떄")
    public void createEvent_BAD_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Developement")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,11,11,11,11,11))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,1,1,1,1,1))
                .beginEventDateTime(LocalDateTime.of(2020,2,2,2,2,2))
                .endEventDateTime(LocalDateTime.of(2020,3,3,3,3,3))
                .basePrice(100)
                .maxPrice(200)
                .limitOffEnrollment(100)
                .location("강남역")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();


        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    @TestDescription("빈값이 들어왔을떄 테스트")
    public void createEvent_Bad_request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("잘못 된 값이 들어왔을떄 테스트")
    public void createEvent_Bad_request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Developement")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,11,11,11,11,11))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,11,11,11,11,11))
                .beginEventDateTime(LocalDateTime.of(2020,11,11,11,11,11))
                .endEventDateTime(LocalDateTime.of(2020,11,11,11,11,11))
                .basePrice(100000)
                .maxPrice(200)
                .limitOffEnrollment(100)
                .location("강남역")
                .build();


        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
        ;


    }

    @Test
    @TestDescription("30개 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception{
        //given
        IntStream.range(0, 30).forEach(this::generateEvent);

        //When

        this.mockMvc.perform(get("/api/events")
                    .param("page", "1")
                    .param("size","10")
                    .param("sort", "name,DESC")
                 )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventModelList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
//                .andExpect(jsonPath("_links.profile").exists())
//                .andDo(document("query_events"))

        ;
    }

    private void generateEvent(int index) {
        Event event = Event.builder()
                .name("event" + index)
                .description("test event")
                .build();
        this.eventRepository.save(event);
    }

}
