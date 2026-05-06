package com.back.domain.member.service;

import com.back.domain.member.entity.Member
import com.back.domain.member.repository.MemberRepository
import com.back.global.exception.ServiceException
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Consumer

@Service
@RequiredArgsConstructor
class MemberService(

    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Autowired
    private lateinit var authTokenService: AuthTokenService

    fun join(
        username: String, password: String, nickname: String, apiKey: String = UUID.randomUUID().toString()
    ): Member {
        findByUsername(username).ifPresent {
            Consumer{ m: Member -> throw ServiceException("409-1", "이미 사용중인 아이디입니다.") }
        }
        val member: Member = Member(username, passwordEncoder.encode(password)!!, nickname, apiKey)
        return memberRepository.save<Member>(member)
    }

    fun count(): Long {
        return memberRepository.count()
    }

    fun findByUsername(username: String) : Optional<Member>{
        return memberRepository.findByUsername(username)
    }

    fun findByApiKey(apiKey: String): Optional<Member?> {
        return memberRepository.findByApiKey(apiKey)
    }

    fun genAccessToken(member: Member): String {
        return authTokenService.genAccessToken(member)
    }

    fun payloadOrNull(jwt: String): Map<String,Any>? {
        return authTokenService.payloadOrNull(jwt)
    }

    fun findById(id: Int): Optional<Member>{
        return memberRepository.findById(id)
    }

    fun findAll(): List<Member> {
        return memberRepository.findAll();
    }

    fun checkPassword(inputPassword: String, rawPassword: String) {
        if(!passwordEncoder.matches(inputPassword, rawPassword)) {
            throw ServiceException("401-2", "비밀번호가 일치하지 않습니다.");
        }
    }
}
