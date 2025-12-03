package com.bookstore.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bookstore.app.dto.ModerationRequestDTO;
import com.bookstore.app.model.Author;
import com.bookstore.app.model.Book;
import com.bookstore.app.model.BookImage;
import com.bookstore.app.model.Genre;
import com.bookstore.app.model.ModerationRequest;
import com.bookstore.app.model.ModerationStatus;
import com.bookstore.app.repo.ModerationRequestRepo;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ModerationServiceTests {

    @Mock
    BookService bookService;
    @Mock
    AuthorService authorService;
    @Mock
    BookImageService bookImageService;
    @Mock
    CloudinaryService cloudinaryService;
    @Mock
    private ModerationRequestRepo moderationRequestRepo;
    @InjectMocks
    private ModerationService moderationService;

    @Test
    public void testApproveModerationRequest() {
        Genre genre = new Genre();
        genre.setName("FANTASY");
        Long moderationRequestId = 1L;
        ModerationRequest request = new ModerationRequest();
        request.setImagePublicId("publicId");
        request.setImageUrl("imageUrl");
        request.setTitle("title");
        request.setDescription("description");
        request.setPrice(19.99);
        request.setGenre(genre);

        Author author = new Author();
        author.setName("author");
        request.setAuthor(author);

        when(moderationRequestRepo.findById(moderationRequestId)).thenReturn(Optional.of(request));
        when(authorService.getAuthorByAuthorName(author.getName())).thenReturn(Optional.of(author));

        moderationService.approveModerationRequest(moderationRequestId);

        assertEquals(ModerationStatus.APPROVED, request.getStatus());
        verify(bookService, times(1)).saveBook(any(Book.class), eq(author), any(BookImage.class));

        verify(moderationRequestRepo, times(1)).save(request);
    }

    @Test
    public void testApproveModerationRequestThrowsNotFound() {
        Long id = 1L;
        when(moderationRequestRepo.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> moderationService.approveModerationRequest(id));


        assertEquals("Moderation request not found", exception.getMessage());
        verify(moderationRequestRepo, never()).save(any(ModerationRequest.class));
        verify(moderationRequestRepo, times(1)).findById(id);
    }

    @Test
    public void testRejectModerationRequest() throws IOException {
        Long moderationRequestId = 1L;
        String reason = "reasonR";

        ModerationRequest request = new ModerationRequest();
        request.setImagePublicId("publicId");

        when(moderationRequestRepo.findById(moderationRequestId)).thenReturn(Optional.of(request));

        moderationService.rejectModerationRequest(moderationRequestId, reason);

        assertEquals(ModerationStatus.REJECTED, request.getStatus());
        verify(cloudinaryService).deleteFile(request.getImagePublicId());
        verify(moderationRequestRepo, times(1)).save(request);
    }

    @Test
    public void testRejectModerationRequestThrowsNotFound() {
        Long id = 1L;
        String reason = "reason";
        when(moderationRequestRepo.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> moderationService.rejectModerationRequest(id, reason));


        assertEquals("Moderation request not found", exception.getMessage());
        verify(moderationRequestRepo, never()).save(any(ModerationRequest.class));
        verify(moderationRequestRepo, times(1)).findById(id);
    }

    @Test
    public void testFindAllRequestsByUsername() {
        Genre genre = new Genre();
        genre.setName("FANTASY");
        Author author = new Author();
        String username = "username";
        author.setName(username);

        ModerationRequest request1 = new ModerationRequest();
        request1.setId(1L);
        request1.setTitle("Book 1");
        request1.setStatus(ModerationStatus.PENDING);
        request1.setGenre(genre);

        ModerationRequest request2 = new ModerationRequest();
        request2.setId(2L);
        request2.setTitle("Book 2");
        request2.setStatus(ModerationStatus.APPROVED);
        request2.setGenre(genre);

        List<ModerationRequest> mockRequests = Arrays.asList(request1, request2);

        when(authorService.getAuthorByUsername(username)).thenReturn(author);
        when(moderationRequestRepo.findByAuthor(author)).thenReturn(mockRequests);

        List<ModerationRequestDTO> result =
                moderationService.findAllRequestsByUsername(username);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("PENDING", result.get(0).getStatus());

        assertEquals(2L, result.get(1).getId());
        assertEquals("Book 2", result.get(1).getTitle());
        assertEquals("APPROVED", result.get(1).getStatus());

        // Verify interactions
        verify(authorService, times(1)).getAuthorByUsername(username);
        verify(moderationRequestRepo, times(1)).findByAuthor(author);
    }

}
