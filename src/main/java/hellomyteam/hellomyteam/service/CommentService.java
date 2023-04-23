package hellomyteam.hellomyteam.service;

import hellomyteam.hellomyteam.dto.*;
import hellomyteam.hellomyteam.entity.Board;
import hellomyteam.hellomyteam.entity.Comment;
import hellomyteam.hellomyteam.entity.TeamMemberInfo;
import hellomyteam.hellomyteam.entity.status.BoardAndCommentStatus;
import hellomyteam.hellomyteam.repository.CommentRepository;
import hellomyteam.hellomyteam.repository.TeamMemberInfoRepository;
import hellomyteam.hellomyteam.repository.custom.impl.CommentCustomImpl;
import hellomyteam.hellomyteam.repository.custom.impl.TeamCustomImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;

import static hellomyteam.hellomyteam.dto.CommentResDto.convertCommentToDto;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TeamMemberInfoRepository teamMemberInfoRepository;
    private final BoardService boardService;
    private final CommentCustomImpl commentCustomImpl;
    private final TeamCustomImpl teamCustomImpl;
    private final EntityManager em;


    public CommonResponse<?> findCommentsByBoard(Long boardId, int commentNum, int commentSize) {
        List<CommentResDto> commentResDtoList = new ArrayList<>();
        Map<Long, CommentResDto> map = new HashMap<>();

        Board findBoard = boardService.getBoardById(boardId);
        if (null == findBoard) {
            return CommonResponse.createError("존재하지 않는 게시글 id 입니다.");
        }

        //로그인 사용자 정보 받아옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        List<Long> currentIds = teamCustomImpl.findCurrentIdByLoginUser(currentUserEmail);

        //페이징 처리 객체 생성
        Pageable pageable = PageRequest.of(commentNum, commentSize);
        List<Comment> comments = commentCustomImpl.findCommentByBoard(findBoard, pageable);

        //댓글 컨버팅 계층형 댓글 및 삭제 댓글 상태 변경
        comments.stream().forEach(c -> {
            String imageUrl = commentCustomImpl.getWriterImgUrl(c.getTeamMemberInfo().getId());
            CommentResDto dto = convertCommentToDto(c);
            dto.setImgUrl(imageUrl);

            // 댓글 계층 생성
            map.put(dto.getCommentId(), dto);
            if (c.getParent() != null) {
                map.get(c.getParent().getId()).getChildren().add(dto);
            } else {
                commentResDtoList.add(dto);
            }

            //작성자 판별
            if (currentIds.contains(dto.getTeamMemberInfoId())) {
                dto.setAuthor(true);
            } else {
                dto.setAuthor(false);
            }
        });

        int fromIndex = (int) pageable.getOffset();
        int toIndex = Math.min(fromIndex + pageable.getPageSize(), commentResDtoList.size());

        List<CommentResDto> subList = commentResDtoList.subList(fromIndex, toIndex);
        return CommonResponse.createSuccess(new PageImpl<>(subList, pageable, commentResDtoList.size()), "comment 리스트 success");
    }

    public CommonResponse<?> createComment(Long boardId, CommentCreateReqDto commentCreateReqDto) {
        if (boardId == null) {
            return CommonResponse.createError("boardId가 누락되었습니다.");
        }

        Board board = em.find(Board.class, boardId);

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
                .likeCount(0)
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
                    .likeCount(comment.getLikeCount())
                    .createdDate(comment.getCreatedDate())
                    .modifiedDate(comment.getModifiedDate())
                    .parentId(comment.getParent().getId())
                    .build();
        } else {
            commentCreateResDto = CommentCreateResDto.builder()
                    .commentId(comment.getId())
                    .writer(comment.getWriter())
                    .content(comment.getContent())
                    .likeCount(comment.getLikeCount())
                    .createdDate(comment.getCreatedDate())
                    .modifiedDate(comment.getModifiedDate())
                    .build();
        }

        board.setCommentCount(board.getCommentCount() + 1);
        return CommonResponse.createSuccess(commentCreateResDto, "댓글 작성 success");
    }

    public CommonResponse<?> updateComment(Long commentId, CommentUpdateReqDto commentUpdateReqDto) {
        Comment findComment = em.find(Comment.class, commentId);

        if (!findComment.getTeamMemberInfo().getId().equals(commentUpdateReqDto.getTeamMemberInfoId())) {
            System.out.println("findComment.getTeamMemberInfo().getId() = " + findComment.getTeamMemberInfo().getId());
            System.out.println("commentUpdateReqDto = " + commentUpdateReqDto.getTeamMemberInfoId());
            return CommonResponse.createError("수정 권한이 없습니다!.");
        }

        if (findComment.getCommentStatus().equals(BoardAndCommentStatus.DELETE_USER)) {
            return CommonResponse.createError("삭제된 댓글은 수정할 수 없습니다.");
        }

        findComment.setContent(commentUpdateReqDto.getContent());


        return CommonResponse.createSuccess(findComment, "수정되었습니다.");
    }

    public void deleteComment(Long commentId) {
        Comment findComment = em.find(Comment.class, commentId);
        findComment.setCommentStatus(BoardAndCommentStatus.DELETE_USER);
    }
}


