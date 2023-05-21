package hellomyteam.hellomyteam.service;

import hellomyteam.hellomyteam.config.S3Uploader;
import hellomyteam.hellomyteam.dto.CommonResponse;
import hellomyteam.hellomyteam.dto.ImgProfileResDto;
import hellomyteam.hellomyteam.entity.Board;
import hellomyteam.hellomyteam.entity.Image;
import hellomyteam.hellomyteam.entity.Team;
import hellomyteam.hellomyteam.entity.TeamMemberInfo;
import hellomyteam.hellomyteam.repository.BoardRepository;
import hellomyteam.hellomyteam.repository.FileUploadRepository;
import hellomyteam.hellomyteam.repository.TeamMemberInfoRepository;
import hellomyteam.hellomyteam.repository.TeamRepository;
import hellomyteam.hellomyteam.repository.custom.impl.FileUploadCustomImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final TeamRepository teamRepository;
    private final BoardRepository boardRepository;
    private final TeamMemberInfoRepository teamMemberInfoRepository;
    private final FileUploadRepository fileUploadRepository;
    private final FileUploadCustomImpl fileUploadCustomImpl;
    private final S3Uploader s3Uploader;



    /**
     * 팀 로고 이미지 저장
     * @param multipartFile
     * @param teamId
     * @return
     * @throws IOException
     */
    public List saveLogo(MultipartFile multipartFile, Long teamId) throws IOException {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("teamId가 누락되었습니다."));

        if (!multipartFile.isEmpty()) {
            Map<String, String> storedFileURL = s3Uploader.upload(multipartFile, "teamLogo");
            String fileName = storedFileURL.get("fileName");
            String uploadImageUrl = storedFileURL.get("uploadImageUrl");

            Image image = Image.builder()
                    .team(team)
                    .imageUrl(uploadImageUrl)
                    .storeFilename(fileName)
                    .build();

            Boolean result = fileUploadRepository.existsImageByTeamId(teamId);
            //존재=true
            if (result) {
                fileUploadCustomImpl.updateLogoByTeam(teamId, image.getImageUrl(), image.getStoreFilename());
            } else {
                fileUploadRepository.save(image);
            }
        }

        List<Image> image =  fileUploadRepository.findImageByTeamId(teamId);
        return image;
    }

    /**
     * 로고 삭제
     * @param teamId
     * @return
     */
    public List<Image> deleteLogoByTeamId(Long teamId) {
        fileUploadCustomImpl.changeImageByTeamId(teamId);
        List<Image> image =  fileUploadRepository.findImageByTeamId(teamId);
        return image;
    }

    /**
     * 팀멤버 프로필 저장 - 팀 정보 페이지
     * @param multipartFile
     * @param teamMemberInfoId
     * @return
     * @throws IOException
     */
    public CommonResponse<?> saveProfile(MultipartFile multipartFile, Long teamMemberInfoId) throws IOException {
        TeamMemberInfo teamMemberInfo = teamMemberInfoRepository.findTeamMemberInfoById(teamMemberInfoId);

        if (!multipartFile.isEmpty()) {
            Map<String, String> storedFileURL = s3Uploader.upload(multipartFile, "profile");
            String fileName = storedFileURL.get("fileName");
            String uploadImageUrl = storedFileURL.get("uploadImageUrl");

            Boolean result = fileUploadRepository.existsImageByTeamMemberInfoId(teamMemberInfoId);

            //존재=true
            if (result) {
                fileUploadCustomImpl.updateProfileByTeamMemberInfoId(teamMemberInfoId, uploadImageUrl, fileName);
            } else {
                Image image = Image.builder()
                        .teamMemberInfo(teamMemberInfo)
                        .imageUrl(uploadImageUrl)
                        .storeFilename(fileName)
                        .build();
                fileUploadRepository.save(image);
            }
        }
        return CommonResponse.createSuccess("profile 이미지 저장 success");
    }

    /**
     * 프로필 정보 조회
     * @param teamMemberInfoId
     * @return
     */
    public CommonResponse<?> getProfile(Long teamMemberInfoId) {
        ImgProfileResDto image = fileUploadCustomImpl.getProfileImgByTmiId(teamMemberInfoId);
        return CommonResponse.createSuccess(image, "profile 이미지 조회 결과");
    }

    /**
     * 백그라운드 이미지 저장/수정
     * @param multipartFile
     * @param teamMemberInfoId
     * @return
     */
    public CommonResponse<?> saveBackgroundImg(MultipartFile multipartFile, Long teamMemberInfoId) throws IOException {
        if (!multipartFile.isEmpty()) {
            Map<String, String> storedFileURL = s3Uploader.upload(multipartFile, "background");
            String fileName = storedFileURL.get("fileName");
            String uploadImageUrl = storedFileURL.get("uploadImageUrl");

            Boolean result = fileUploadRepository.existsImageByTeamMemberInfoBackGroundId(teamMemberInfoId);

            //존재=true
            if (result) {
                fileUploadCustomImpl.updateBackgroundByTeamMemberInfoId(teamMemberInfoId, uploadImageUrl, fileName);
            } else {
                Image image = Image.builder()
                        .teamMemberInfoBackGroundId(teamMemberInfoId)
                        .imageUrl(uploadImageUrl)
                        .storeFilename(fileName)
                        .build();
                fileUploadRepository.save(image);
            }
        }
        return CommonResponse.createSuccess("background 이미지 저장 success");
    }

    /**
     * 백그라운드 정보 조회
     * @param teamMemberInfoId
     * @return
     */
    public CommonResponse<?> getBackgroundImg(Long teamMemberInfoId) {
        ImgProfileResDto image = fileUploadCustomImpl.getBackgroundImgByTmiId(teamMemberInfoId);
        return CommonResponse.createSuccess(image, "background 이미지 조회 결과");
    }

    //TODO 게시판 이미지 저장
    /**
     * 게시판 이미지 저장
     * @param teamId, boardId, file
     * @return uploadImageUrl
     */
    public CommonResponse<?> saveBoardImage(Long teamId, Long boardId, MultipartFile multipartFile) throws IOException{
        if(!multipartFile.isEmpty()){
            Map<String, String> storedFileURL = s3Uploader.upload(multipartFile, "boardImg"+boardId);

            String fileName = storedFileURL.get("fileName");
            String uploadImageUrl = storedFileURL.get("uploadImageUrl");

            Team team = teamRepository.findById(teamId)
                    .orElseThrow(() -> new IllegalArgumentException("teamId가 누락되었습니다."));
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("boardId가 누락되었습니다."));

            Image image = Image.builder()
                    .team(team)
                    .board(board)
                    .imageUrl(uploadImageUrl)
                    .storeFilename(fileName)
                    .build();
            fileUploadRepository.save(image);
            return CommonResponse.createSuccess(uploadImageUrl);
        }
        return CommonResponse.createError("에러발생");
    }
}
