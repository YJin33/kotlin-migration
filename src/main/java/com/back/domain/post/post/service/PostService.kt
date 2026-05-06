package com.back.domain.post.post.service;

import com.back.domain.member.entity.Member
import com.back.domain.post.post.entity.Post
import com.back.domain.post.post.repository.PostRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class PostService(
    private val postRepository: PostRepository
) {
    @Transactional
    fun write(author: Member, title: String, content: String): Post {
        val post: Post = Post(author, title, content);
        return postRepository.save(post);
    }

    fun modify(id: Int, title: String, content: String): Post{
        val post: Post = postRepository.findById(id).get();
        post.update(title, content);

        return post
    }
    fun deleteById(id: Int) =
        postRepository.deleteById(id)

    fun findById(id: Int): Post? =
        postRepository.findByIdOrNull(id)

    fun count(): Long =
        postRepository.count()


    fun findAll(): MutableList<Post> =
        postRepository.findAll()

    fun flush() =
        postRepository.flush()

}

