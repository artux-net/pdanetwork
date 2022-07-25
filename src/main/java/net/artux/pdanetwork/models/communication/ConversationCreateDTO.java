package net.artux.pdanetwork.models.communication;

import lombok.Data;

import java.util.List;

@Data
public class ConversationCreateDTO {

    private String title;
    private String icon;
    private List<Long> users;

}
