package net.artux.pdanetwork.service.member;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.MemberDto;
import net.artux.pdanetwork.service.action.ActionService;
import net.artux.pdanetwork.models.Member;
import net.artux.pdanetwork.models.RegisterUser;
import net.artux.pdanetwork.controller.UserValidator;
import net.artux.pdanetwork.models.*;
import net.artux.pdanetwork.models.profile.Data;
import net.artux.pdanetwork.repository.MemberRepository;
import net.artux.pdanetwork.service.email.EmailService;
import net.artux.pdanetwork.service.util.PageService;
import net.artux.pdanetwork.service.util.SortService;
import net.artux.pdanetwork.utills.Security;
import net.artux.pdanetwork.service.util.Utils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.Timer;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;

  private final ActionService actionService;
  private final EmailService emailService;
  private final SortService sortService;
  private final PageService pageService;

  private final UserValidator userValidator;
  private final PasswordEncoder passwordEncoder;

  private final MemberMapper memberMapper;

  private static final Map<String, RegisterUser> registerUserMap = new HashMap<>();

  @Override
  public Status registerUser(RegisterUser newUser) {
    Status status = userValidator.checkUser(newUser);

    String token = Security.encrypt(newUser.getLogin());

    if (status.isSuccess()) {
      if (!registerUserMap.containsKey(token))
        try {
            emailService.sendConfirmLetter(newUser, token);
            addCurrent(token, newUser);
            status = new Status(true, "Проверьте почту.");
        } catch (Exception e) {
          status = new Status(false, "Не удалось отправить письмо на " + newUser.getEmail());
        }
      else
        status = new Status(false, "Пользователь ожидает регистрации, проверьте почту.");
    }

    return status;
  }

  private void addCurrent(String token, RegisterUser user){
    System.out.println("Add to register wait list with token: " + token + ", " + user.getEmail());
    registerUserMap.put(token, user);
    new Timer(30*60*1000, e -> registerUserMap.remove(token)).start();
  }

  public Status handleConfirmation(String token){
    if (registerUserMap.containsKey(token)) {
      Optional<Member> member = memberRepository.findTopByOrderByPdaIdDesc();
      int pdaId = 1;
      if (member.isPresent())
        pdaId = member.get().getPdaId() + 1;
      memberRepository.insert(new Member(registerUserMap.get(token), pdaId, passwordEncoder));
      try {
        emailService.sendRegisterLetter(registerUserMap.get(token), pdaId);
        registerUserMap.remove(token);
        return new Status(true, pdaId + " - Это ваш pdaId, мы вас зарегистрировали, спасибо!");
      } catch (Exception e) {
        return new Status(true, "Не получилось отправить подтверждение на почту, но мы вас зарегистрировали, спасибо!");
      }
    }else return new Status(false, "Ссылка устарела или не существует");
  }

  @Override
  public MemberDto resetData() {
    Member member = getMember();
    member.setMoney(500);
    member.setData(new Data());
    memberRepository.save(member);
    return memberMapper.memberDto(member);
  }

  @Override
  public Member getMember() {
    return memberRepository.getMemberByLogin(SecurityContextHolder.getContext()
            .getAuthentication()
            .getName())
            .orElseThrow(()->new UsernameNotFoundException("Пользователя не существует"));
  }

  @Override
  public MemberDto getMemberDto() {
    return memberMapper.memberDto(getMember());
  }

  @Override
  public Member getMember(String base64) {
    if (Utils.isEmpty(base64))
      return null;
    base64 = base64.replaceFirst("Basic ", "");
    base64 = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
    String login = base64.split(":")[0];
    String password = base64.split(":")[1];
    Optional<Member> optionalMember = memberRepository.getMemberByLogin(login);
    if (optionalMember.isPresent()
            && passwordEncoder.matches(password, optionalMember.get().getPassword()))
      return optionalMember.get();
    else return null;
  }

  @Override
  public Member getMember(ObjectId objectId) {
    return memberRepository.getMemberBy_id(objectId).orElseThrow(()->new RuntimeException("Пользователя не существует"));
  }

  @Override
  public Member getMemberByPdaId(Integer id) {
    return memberRepository.getMemberByPdaId(id).orElseThrow(()->new RuntimeException("Пользователя не существует"));
  }

  @Override
  public Member getMemberByEmail(String email) {
    return memberRepository.getMemberByEmail(email).orElseThrow(()->new RuntimeException("Пользователя не существует"));
  }


  @Override
  public Status editMember(RegisterUser user){
    Member member = getMember();

    Status status = userValidator.checkUser(user);
    if (status.isSuccess()){
      member.setName(user.getName());
      member.setNickname(user.getNickname());
      member.setLogin(user.getLogin());
      //TODO email may be wrong
      //old.setEmail(user.getEmail());
      member.setAvatar(user.getAvatar());
      member.setPassword(passwordEncoder.encode(user.getPassword()));
      memberRepository.save(member);
      status.setDescription("Данные изменены");
    }

    return status;
  }

  @Override
  public Member saveMember(Member member) {
    return memberRepository.save(member);
  }

  @Override
  public ResponsePage<UserInfo> getRating(QueryPage queryPage) {
    Page<Member> memberPage = memberRepository.findAll(
            sortService.getSortInfo(UserInfo.class, queryPage, "xp"));
    return pageService.mapDataPageToResponsePage(memberPage, memberMapper.info(memberPage.getContent()));
  }

  @Override
  public Member doActions(HashMap<String, List<String>> actions) {
    return memberRepository.save(actionService.doUserActions(actions, getMember()));
  }
}
