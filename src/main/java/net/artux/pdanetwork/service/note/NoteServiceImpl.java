package net.artux.pdanetwork.service.note;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.models.*;
import net.artux.pdanetwork.models.profile.Note;
import net.artux.pdanetwork.repository.MemberRepository;
import net.artux.pdanetwork.service.member.MemberService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

  private final MemberService memberService;
  private final MemberRepository memberRepository;

  @Override
  public List<Note> getNotes() {
    Member member = memberService.getMember();
    return member.getNotes();
  }

  @Override
  public Note createNote(String title) {
    Member member = memberService.getMember();
    Note note = member.addNote(title, "");
    memberRepository.save(member);
    return note;
  }

  @Override
  public Note editNote(Note note) {
    Member member = memberService.getMember();
    for (int i = 0; i < member.notes.size(); i++)
      if (member.notes.get(i).cid == note.cid) {
        member.notes.set(i, note);
        return note;
      }
    note = member.addNote(note.title, note.content);
    memberRepository.save(member);
    return note;
  }

  @Override
  public Status deleteNote(Integer id) {
    Member member = memberService.getMember();
    for (int i = 0; i < member.notes.size(); i++)
      if (member.notes.get(i).cid == id) {
        member.notes.remove(i);
        memberRepository.save(member);
        return new Status(true, "ok");
      }
    throw new RuntimeException("Wrong id");
  }
}
