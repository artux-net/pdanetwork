package net.artux.pdanetwork.service;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.Member;
import net.artux.pdanetwork.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MongoUserDetailService implements UserDetailsService {

  @Autowired
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = memberRepository.getMemberByLogin(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

    List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRole()));

    return new User(member.getLogin(), member.getPassword(), authorities);
  }
}
