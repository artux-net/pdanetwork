package net.artux.pdanetwork.service

import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.models.note.NoteCreateDto
import net.artux.pdanetwork.models.note.NoteDto
import net.artux.pdanetwork.repository.user.NoteRepository
import net.artux.pdanetwork.service.note.NoteServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.security.test.context.support.WithUserDetails

@SpringBootTest
@WithUserDetails(value = "admin@artux.net")
class NoteServiceTest : AbstractTest() {

    @SpyBean
    lateinit var noteRepository: NoteRepository

    @Autowired
    lateinit var noteService: NoteServiceImpl

    @Test
    fun testEditNote() {
        // Arrange
        val note = NoteCreateDto("Test Note", "This is a test note.")
        val id = noteService.createNote(note).id
        // Act
        val result: NoteDto = noteService.editNote(id, note)

        // Assert
        Assertions.assertEquals(note.title, result.title)
        Assertions.assertEquals(note.content, result.content)
        verify(noteRepository, times(2)).save(any())
    }
}
