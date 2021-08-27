package net.artux.pdanetwork.servlets;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.models.profile.Note;
import net.artux.pdanetwork.utills.ServletHelper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static net.artux.pdanetwork.utills.ServletHelper.getBody;
import static net.artux.pdanetwork.utills.ServletHelper.getMember;
import static net.artux.pdanetwork.utills.mongo.MongoUsers.updateMember;

//@WebServlet("/notes")
public class NoteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        //updating note
        Member member = getMember(httpServletRequest);
        if (member != null) {
            Note updatedNote = getBody(httpServletRequest, Note.class);
            boolean found = false;
            for (int i = 0; i < member.notes.size(); i++)
                if (member.notes.get(i).cid == updatedNote.cid) {
                    found = true;
                    member.notes.set(i, updatedNote);
                    break;
                }
            if (!found)
                member.addNote(updatedNote.title, updatedNote.content);
            updateMember(member);
            ServletHelper.setResponse(httpServletResponse, member);
        }
    }

    @Override
    protected void doPut(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        //creating note
        Member member = getMember(httpServletRequest);
        if (member != null) {
            member.addNote("Новая заметка", ServletHelper.getString(httpServletRequest));
            updateMember(member);
            ServletHelper.setResponse(httpServletResponse, member);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        //deleting note
        Member member = getMember(httpServletRequest);
        if (member != null) {
            int cid = Integer.parseInt(httpServletRequest.getParameter("cid"));
            for (int i = 0; i < member.notes.size(); i++)
                if (member.notes.get(i).cid == cid) {
                    member.notes.remove(i);
                    break;
                }
            updateMember(member);
            ServletHelper.setResponse(httpServletResponse, member);
        }
    }
}
