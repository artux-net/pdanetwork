
package net.artux.pdanetwork.entity.items;


import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "user_usual_item")
public class UsualItemEntity extends ItemEntity {

}
