package edu.msu.mi.forum;

import edu.msu.mi.forum.model.Conversation;
import edu.msu.mi.forum.model.Corpus;
import edu.msu.mi.forum.model.DiscussionThread;
import edu.msu.mi.forum.model.Post;
import edu.msu.mi.forum.util.*;
import edu.msu.mi.forum.replies.*;

import java.util.List;

/**
 * Created by aditya on 10/2/16.
 */
public class implementationClass extends InferReplies {

    public static void main(String[] args) {
        Corpus corpus = new Corpus() {
            public Conversation getConversation() {
                return null;
            }

            public Iterable<Post> getPostsByAuthor() {
                return null;
            }

            public Iterable<Post> getRepliesByFirstPoster() {
                return null;
            }
        };
        Conversation conversation = new Conversation() {
            public List<DiscussionThread> getAllThreads() {
                return null;
            }
        };

        InferReplies inferreplies = new InferReplies(corpus, false);
        conversation = inferreplies.getConversation();

    }
}




