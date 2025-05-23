package com.example.layered.service;

import com.example.layered.Dto.MemoRequestDto;
import com.example.layered.Dto.MemoResponseDto;
import com.example.layered.entitey.Memo;
import com.example.layered.repository.MemoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Annotation @Service는 @Component와 같다, Spring Bean으로 등록한다는 뜻.
 * Spring Bean으로 등록되면 다른 클래스에서 주입하여 사용할 수 있다.
 * 명시적으로 Service Layer 라는것을 나타낸다.
 * 비지니스 로직을 수행한다.
 */
@Service
public class MemoServiceImpl implements MemoService {

    private final MemoRepository memoRepository;

    public MemoServiceImpl(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }


    @Override
    public MemoResponseDto saveMemo(MemoRequestDto dto) {

        // 요청받은 데이터로 MEMO 객체 생성 ID 없음
        Memo memo = new Memo(dto.getTitle(), dto.getContents());

        // DB 저장
        Memo savedMemo = memoRepository.saveMemo(memo);

        return new MemoResponseDto(savedMemo);
    }

    @Override
    public List<MemoResponseDto> findAllMemos() {

         List<MemoResponseDto> allMemos = memoRepository.findAllMemos();

        return allMemos;
    }

    @Override
    public MemoResponseDto findMemoById(Long id) {
        // 식별자의 Memo가 없다면?
        Memo memo = memoRepository.findMemoById(id);

        // 아래 코드 사용 불가.
        // if (memo == null) {
        //     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // }

        // NPE 방지
        if (memo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id" + id);
        }

        return new MemoResponseDto(memo);
    }

    @Override
    public MemoResponseDto updateMemo(Long id, String title, String contents) {
        // memo 조회
        Memo memo = memoRepository.findMemoById(id);

        // NPE 방지
        if (memo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
        // 필수값 검증
        if (title == null || contents == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The title is a required value.");
        }

        // memo 수정
        memo.update(title, contents);

        return new MemoResponseDto(memo);
    }

    @Override
    public MemoResponseDto updateTitle(Long id, String title, String contents) {
        // memo 조회
        Memo memo = memoRepository.findMemoById(id);

        // NPE 방지
        if (memo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
        // 필수값 검증
        if (title == null || contents != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The title is a required value.");
        }

        memo.updateTitle(title);

        return new MemoResponseDto(memo);
    }

    @Override
    public void deleteMemo(Long id) {
        // memo 조회
        Memo memo = memoRepository.findMemoById(id);

        // NPE 방지
        if (memo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
        memoRepository.deleteMemo(id);

    }

}
