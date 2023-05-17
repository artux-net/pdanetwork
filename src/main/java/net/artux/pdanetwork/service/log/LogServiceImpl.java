package net.artux.pdanetwork.service.log;

import net.artux.pdanetwork.models.page.ResponsePage;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class LogServiceImpl implements LogService {


    @Value("${logging.file.name}")
    private String fileName;

    public String readLogs() {
        String logs;
        try {
            logs = IOUtils.toString(new FileInputStream(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logs = e.getMessage();
            e.printStackTrace();
        }

        return logs;

    }

    @Override
    public ResponsePage<Object> getLogs(Optional<Integer> page, Optional<Integer> size) {
        String logs = readLogs();
        int maxPageSize = size.orElse(300);
        int pageIndex = page.orElse(1);
        Stream<String> lines = logs.lines();
        long linesCount = lines.count();

        List<Object> responseLog = lines
                .skip(linesCount - maxPageSize * pageIndex)
                .limit(maxPageSize)
                .collect(Collectors.toUnmodifiableList());

        return ResponsePage.builder()
                .lastPage((int) (linesCount / maxPageSize))
                .content(responseLog)
                .contentSize(responseLog.size())
                .number(pageIndex)
                .totalSize(linesCount)
                .build();
    }
}
