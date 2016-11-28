package edu.msu.mi.forum.webmd


import edu.msu.mi.forum.model.Conversation
import edu.msu.mi.forum.model.Corpus
import edu.msu.mi.forum.model.DiscussionThread
import edu.msu.mi.forum.model.Post
import edu.msu.mi.forum.model.impl.DiscussionThreadImpl
import edu.msu.mi.forum.model.impl.PostImpl
import edu.msu.mi.forum.replies.InferReplies
import groovy.sql.Sql
import groovy.time.TimeCategory

/**
 * Created by anpwr8 on 11/9/2016.
 */
class ImplementationClass extends InferReplies {

    public ImplementationClass() {
        super();
    }

    // COLS
    static Map COLS = [(WebMdConversation.Column.NAME)  : "name", (WebMdConversation.Column.AUTHORID): "authorid", (WebMdConversation.Column.URL): "url",(WebMdConversation.Column.THREAD): "thread",  (WebMdConversation.Column.POSTID): "postid",(WebMdConversation.Column.TEXT): "post_text", (WebMdConversation.Column.AUTHOR): "author", (WebMdConversation.Column.REPLYTOID): "reply_to_id", (WebMdConversation.Column.SEQUENCE): "sequencenum", (WebMdConversation.Column.USERNAME): "username", (WebMdConversation.Column.SUBJECT): "subject", (WebMdConversation.Column.POSTDATE): "postdate"]

    static ConfigObject props;

    public static Sql getSqlConnection() {
        Sql.newInstance("jdbc:postgresql://${props.host}:${props.port}/${props.database}", props.username, props.password, 'org.postgresql.Driver')
    }

    /* now I need to pass in the query to get the corpus
    *
    *
    *
    */

    def static read(Sql s, String query, Map cols){

        Map<String, DiscussionThread> threads = [:]
        s.eachRow(query) { it ->

            def d = cols[Column.POSTDATE] ? new Date(((java.sql.Timestamp) it[cols[Column.POSTDATE]]).getTime()) : new Date((it[cols[Column.POSTDATE]] as Long) * 1000l)
            //Post p = new PostImpl(it[cols[Column.ID]] as String, null, it[cols[Column.AUTHOR]].trim(), d)
            Post p = new PostImpl(it[cols[Column.POSTID]] as String, null, it[cols[Column.USERNAME]], d)
            p.setContent(it[cols[Column.TEXT]])
            String thread = it[cols[Column.THREAD]] as String;
            if (!threads[thread]) {
                threads[thread] = new DiscussionThreadImpl(thread,it[cols[Column.SUBJECT]])
            }
            //p.replyToId = it[cols[Column.REPLY]]
            threads[thread].addPost(p)


            }
            threads.each { k, v ->
                addThread(v)
                }
        }


    public static Corpus getCorpus(String name) {
        getCorpora().collectEntries {
            [it.name, it]
        }[name] ?: null
    }

    public static test(){

        Corpus corpus = getCorpus("somevalue")
        InferReplies inferringReplies = new InferReplies(corpus, false)
        String shortQuery = "select  forum.\"name\" as name, thread.author_id as authorid, thread.url as url, post.thread as thread,post.id as postid, post.post_text as post_text, post.author as author ,post.\"replyto_id\" as reply_to_id , post.\"sequence\" as sequencenum, users.username as username, thread.title as subject, post.\"date\" as postdate  from public.\"Forum\" as forum, public.\"Thread\" as thread, public.\"Post\" as post, public.\"User\" as users where forum.id = thread.forum_id and thread.id = post.thread and post.author = users.id and thread.id = 184023" as String
        read(getSqlConnection(),shortQuery,COLS)
    }

    public static void main(String[] args){
        test()

        }


}
