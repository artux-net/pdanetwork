package net.artux.pdanetwork.models;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class MemberMapperHelper {

    public String map(ObjectId value) {
        return value.toString();
    }

}
