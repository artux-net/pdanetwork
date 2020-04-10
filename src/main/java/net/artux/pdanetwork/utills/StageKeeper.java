package net.artux.pdanetwork.utills;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StageKeeper {

    public static String getChapter(int story, int chapter) throws Exception{
        return readFile( "stories" + "/story_"+ story + "/chapter_" + chapter + ".cqe", StandardCharsets.UTF_8);
    }

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
