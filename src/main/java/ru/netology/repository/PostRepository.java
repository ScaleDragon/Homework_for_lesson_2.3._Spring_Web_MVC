package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
@Repository
public class PostRepository implements InterfacePostRepository {

    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(0);

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {  // Создание нового поста
            long newId = currentId.incrementAndGet();
            Post newPost = new Post(newId, post.getContent());
            posts.put(newId, newPost);
            return newPost;
        } else {  // Обновление существующего поста
            Post existingPost = posts.get(post.getId());
            if (existingPost != null) {
                existingPost.setContent(post.getContent());
                return existingPost;
            } else {
                // Если пост с таким ID не найден, можно выбросить исключение или вернуть null
                throw new NotFoundException("Post with ID " + post.getId() + " not found");
            }
        }
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}
