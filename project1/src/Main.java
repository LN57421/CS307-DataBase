import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException{
        String jsonStringsPost = Files.readString(Path.of("resource/posts.json"));
        List<Post> posts = JSON.parseArray(jsonStringsPost,Post.class);
        String jsonStringsReply = Files.readString(Path.of("resource/replies.json"));
        List<Replies> replies = JSON.parseArray(jsonStringsReply,Replies.class);
        posts.forEach(System.out::println);
        posts = new ArrayList<>(posts);
        System.out.println(posts.get(2).getTitle());
        String s = "%s, %s";
        System.out.println(String.format(s, 1L, 2));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String F = format.format(new Date().getTime());
        System.out.println(F);
    }
}
