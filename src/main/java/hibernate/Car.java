package hibernate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String model;
    private int power;
    private int price;
    private int year;
    @Enumerated(EnumType.STRING)
    private Type type;

    public Car(String model, int power, int price, int year, Type type) {
        this.model = model;
        this.power = power;
        this.price = price;
        this.year = year;
        this.type = type;
    }
}


//Створити клас Car з полями:
//id
//        model,
//Type (ENUM)
//power,
//price,
//year.
