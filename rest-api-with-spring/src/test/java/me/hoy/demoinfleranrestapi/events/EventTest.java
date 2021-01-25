package me.hoy.demoinfleranrestapi.events;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("Infler Spring Rest API")
                .description("REST API development with spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        Event event = new Event();
        String name = "Event";
        String description = "Spring";
        event.setName(name);
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    public void testFree(){
        //Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        //When
        event.update();

        //Then
        assertThat(event.isFree()).isTrue();

        //Given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();
        //When
        event.update();

        //Then
        assertThat(event.isFree()).isFalse();

        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();
        //When
        event.update();

        //Then
        assertThat(event.isFree()).isFalse();
    }

    @Test
    public void testOffline(){
        //Given
        Event event = Event.builder()
                .location("강남영 네이버 D2 스타텁 팩토리")
                .build();
        //When
        event.update();

        //Then
        assertThat(event.isOffline()).isTrue();

        event = Event.builder()
                .build();
        //When
        event.update();

        //Then
        assertThat(event.isOffline()).isFalse();
    }
}