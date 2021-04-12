package me.hoy.demoinfleranrestapi.events;

public class Apple implements Fruit{
    @Override
    public void printName() {
        System.out.println("사과");
    }
}
