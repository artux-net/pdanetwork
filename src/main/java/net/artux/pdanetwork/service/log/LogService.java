package net.artux.pdanetwork.service.log;

import net.artux.pdanetwork.models.page.ResponsePage;

import java.util.Optional;

public interface LogService {

    ResponsePage<Object> getLogs(Optional<Integer> page, Optional<Integer> size);

}
