package me.hoy.demoinfleranrestapi.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.hoy.demoinfleranrestapi.common.RestDocsConfiguration;
import me.hoy.demoinfleranrestapi.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// slice test
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
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
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to updateEvents"),
                                linkWithRel("profile").description("link to updateEvents")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("contents type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new Event"),
                                fieldWithPath("description").description("이벤트 디스크립션"),
                                fieldWithPath("beginEnrollmentDateTime").description("시작시간"),
                                fieldWithPath("closeEnrollmentDateTime").description("끝시간"),
                                fieldWithPath("beginEventDateTime").description("이벤트 디스크립션"),
                                fieldWithPath("endEventDateTime").description("이벤트 디스크립션"),
                                fieldWithPath("location").description("이벤트 디스크립션"),
                                fieldWithPath("basePrice").description("이벤트 디스크립션"),
                                fieldWithPath("maxPrice").description("이벤트 디스크립션"),
                                fieldWithPath("limitOffEnrollment").description("이벤트 디스크립션")

                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id"),
                                fieldWithPath("name").description("Name of new Event"),
                                fieldWithPath("description").description("이벤트 디스크립션"),
                                fieldWithPath("beginEnrollmentDateTime").description("시작시간"),
                                fieldWithPath("closeEnrollmentDateTime").description("끝시간"),
                                fieldWithPath("beginEventDateTime").description("이벤트 디스크립션"),
                                fieldWithPath("endEventDateTime").description("이벤트 디스크립션"),
                                fieldWithPath("location").description("이벤트 디스크립션"),
                                fieldWithPath("basePrice").description("이벤트 디스크립션"),
                                fieldWithPath("maxPrice").description("이벤트 디스크립션"),
                                fieldWithPath("limitOffEnrollment").description("이벤트 디스크립션"),
                                fieldWithPath("free").description("free or not"),
                                fieldWithPath("offline").description("offline"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query events"),
                                fieldWithPath("_links.update-event.href").description("link to update event"),
                                fieldWithPath("_links.profile.href").description("link to update event")
                        )
                ));
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
                .andExpect(jsonPath("_links.index").exists())
        ;


    }

    @Test
    @TestDescription("30개 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception{
        //Given
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
