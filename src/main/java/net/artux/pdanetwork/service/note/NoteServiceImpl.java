package net.artux.pdanetwork.service.note;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.user.UserEntity;
import net.artux.pdanetwork.models.*;
import net.artux.pdanetwork.models.profile.NoteEntity;
import net.artux.pdanetwork.repository.user.UsersRepository;
import net.artux.pdanetwork.service.member.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

  private final MemberService memberService;
  private final UsersRepository usersRepository;

  @Override
  public List<NoteEntity> getNotes() {
    UserEntity userEntity = memberService.getMember();
    return userEntity.getNoteEntities();
  }

  @Override
  public NoteEntity createNote(String title) {
    //TODO notes repo
    /*UserEntity userEntity = memberService.getMember();
    NoteEntity noteEntity = userEntity.addNote(title, "");
    memberRepository.save(userEntity);*/
    return new NoteEntity();
  }

  @Override
  public NoteEntity editNote(NoteEntity noteEntity) {
    UserEntity userEntity = memberService.getMember();
    //TODO edit with notes repo

    /*note.setTime(Instant.now().toEpochMilli());
    for (int i = 0; i < userEntity.notes.size(); i++)
      if (userEntity.notes.get(i).cid == note.cid) {
        userEntity.notes.set(i, note);
        memberRepository.save(userEntity);
        return note;
      }
    note = userEntity.addNote(note.title, note.content);
    memberRepository.save(userEntity);*/
    return noteEntity;
  }

  @Override
  public Status deleteNote(Integer id) {
    //TODO too
    /*UserEntity userEntity = memberService.getMember();
    for (int i = 0; i < userEntity.notes.size(); i++)
      if (userEntity.notes.get(i).cid == id) {
        userEntity.notes.remove(i);
        memberRepository.save(userEntity);
        return new Status(true, "ok");
      }*/
    throw new RuntimeException("damn boy");
  }
}
