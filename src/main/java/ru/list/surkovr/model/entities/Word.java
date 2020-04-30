package ru.list.surkovr.model.entities;


import lombok.*;
import org.bson.types.ObjectId;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Word extends FindableById{

    private ObjectId id;
    private String russian;
    private String english;
    private int difficulty;
}
