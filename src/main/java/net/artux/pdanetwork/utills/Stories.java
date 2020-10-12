package net.artux.pdanetwork.utills;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static net.artux.pdanetwork.utills.ServletContext.getPath;

public class Stories {

    public static String getChapter(int story, int chapter) throws Exception{
        return readFile(getPath() + "stories" + "/story_" + story + "/chapter_" + chapter + ".cqe", StandardCharsets.UTF_8);
    }

    public static String getStories() throws Exception{
        return readFile(getPath() + "stories/info", StandardCharsets.UTF_8);
    }

    public static String getMap(int story, int map) throws Exception {
        return readFile(getPath() + "stories" + "/story_" + story + "/maps/map_" + map + ".sm", StandardCharsets.UTF_8);
    }

    private static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
