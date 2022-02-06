package net.artux.pdanetwork.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue
    protected UUID uid;

}
