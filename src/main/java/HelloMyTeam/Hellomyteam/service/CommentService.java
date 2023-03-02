package HelloMyTeam.Hellomyteam.service;

import HelloMyTeam.Hellomyteam.dto.*;
import HelloMyTeam.Hellomyteam.entity.*;
import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import HelloMyTeam.Hellomyteam.repository.BoardRepository;
import HelloMyTeam.Hellomyteam.repository.CommentRepository;
import HelloMyTeam.Hellomyteam.repository.TeamMemberInfoRepository;
import HelloMyTeam.Hellomyteam.repository.custom.impl.CommentCustomImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;

import static HelloMyTeam.Hellomyteam.dto.CommentResDto.convertCommentToDto;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TeamMemberInfoRepository teamMemberInfoRepository;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final CommentCustomImpl commentCustomImpl;
    private final EntityManager em;


    public CommonResponse<?> findCommentsByBoard(Long boardId) {
        Board findBoard = boardService.getBoardById(boardId);
        if (null == findBoard) {
            return CommonResponse.createError("존재하지 않는 게시글 id 입니다.");
        }

        List<Comment> comments = commentCustomImpl.findCommentByBoard(findBoard);

        List<CommentResDto> commentResDtoList = new ArrayList<>();
        Map<Long, CommentResDto> map = new HashMap<>();

        comments.stream().forEach(c -> {
            CommentResDto dto = convertCommentToDto(c);
            map.put(dto.getCommentId(), dto);
            if(c.getParent() != null) map.get(c.getParent().getId()).getChildren().add(dto);
            else commentResDtoList.add(dto);
        });
        return CommonResponse.createSuccess(commentResDtoList, "계층형 댓글 조회 success");
    }

    public CommonResponse<?> createComment(Long boardId, CommentCreateReqDto commentCreateReqDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("boardId가 누락되었습니다."));
        if (board == null) {
            return CommonResponse.createError("존재하지 않는 게시글입니다.");
        }

        Comment parent = null;
        //자식 댓글 존재시
        if (commentCreateReqDto.getParentId() != null) {
            parent = commentRepository.findCommentById(commentCreateReqDto.getParentId());

            if (null == parent) {
                return CommonResponse.createError("부모댓글과 자식 댓글이 일치하지 않습니다.");
            }
            if (parent.getBoard().getId() != boardId) {
                return CommonResponse.createError("부모댓글과 자식 댓글 게시글 번호가 일치하지 않습니다.");
            }
        }

        TeamMemberInfo teamMemberInfo = teamMemberInfoRepository.findTeamMemberInfoById(commentCreateReqDto.getTeamMemberInfoId());
        Comment comment = Comment.builder()
                .teamMemberInfo(teamMemberInfo)
                .board(board)
                .writer(teamMemberInfo.getMember().getName())
                .commentStatus(BoardAndCommentStatus.NORMAL)
                .content(commentCreateReqDto.getContent())
                .build();

        if (null != parent) {
            comment.updateParent(parent);
        }
        commentRepository.save(comment);

        CommentCreateResDto commentCreateResDto = null;
        if (parent != null) {
            commentCreateResDto = CommentCreateResDto.builder()
                    .commentId(comment.getId())
                    .writer(comment.getWriter())
                    .content(comment.getContent())
                    .createdDate(comment.getCreatedDate())
                    .modifiedDate(comment.getModifiedDate())
                    .parentId(comment.getParent().getId())
                    .build();
        } else {
            commentCreateResDto = CommentCreateResDto.builder()
                    .commentId(comment.getId())
                    .writer(comment.getWriter())
                    .content(comment.getContent())
                    .createdDate(comment.getCreatedDate())
                    .modifiedDate(comment.getModifiedDate())
                    .build();
        }
        return CommonResponse.createSuccess(commentCreateResDto, "댓글 작성 success");
    }

    public CommonResponse<?> updateComment(Long commentId, CommentUpdateReqDto commentUpdateReqDto) {
        Comment findComment = em.find(Comment.class, commentId);
        if (findComment.getTeamMemberInfo().getId() != commentUpdateReqDto.getTeamMemberInfoId()) {
            return CommonResponse.createError("수정 권한이 없습니다.");
        }
        if (findComment.getCommentStatus().equals(BoardAndCommentStatus.DELETE_USER)) {
            return CommonResponse.createError("삭제된 댓글은 수정할 수 없습니다.");
        }

        findComment.setContent(commentUpdateReqDto.getContent());
        return CommonResponse.createSuccess("수정되었습니다.");
    }

    public void deleteComment(Long commentId) {
        Comment findComment = em.find(Comment.class, commentId);
        findComment.setCommentStatus(BoardAndCommentStatus.DELETE_USER);
    }
}
