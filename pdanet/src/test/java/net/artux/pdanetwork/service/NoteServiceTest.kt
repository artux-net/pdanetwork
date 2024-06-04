package net.artux.pdanetwork.service

import net.artux.pdanetwork.AbstractTest
import net.artux.pdanetwork.entity.note.NoteEntity
import net.artux.pdanetwork.models.note.NoteCreateDto
import net.artux.pdanetwork.models.note.NoteDto
import net.artux.pdanetwork.repository.user.NoteRepository
import net.artux.pdanetwork.service.note.NoteServiceImpl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailService")
class NoteServiceTest: AbstractTest() {

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