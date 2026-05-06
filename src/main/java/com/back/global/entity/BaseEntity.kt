package com.back.global.entity;

import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    var id: Int,
) {
    @CreatedDate
    lateinit var createDate: LocalDateTime

    @LastModifiedDate
    lateinit var modifyDate: LocalDateTime

   override fun equals(other: Any?): Boolean {
       if (this === other) return true
       if (other == null || javaClass != other.javaClass) return false
       val that = other as BaseEntity
       return id == that.id
   }
    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}