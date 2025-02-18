package com.snowflakes.rednose.util;

import com.snowflakes.rednose.repository.MemberRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RandomNicknameGenerator {
    private Random random = new Random();
    final String SPACE = " ";

    private final MemberRepository memberRepository;

    final List<String> ADJECTIVES = Arrays.asList(
            "가냘픈", "가는", "가엾은", "가파른", "같은", "거센", "거친", "건조한", "검은", "게으른",
            "게을러빠진", "게을러터진", "고달픈", "고른", "고마운", "고운", "고픈", "곧은", "괜찮은",
            "구석진", "굳은", "굵은", "귀여운", "그런", "그른", "그리운", "기다란", "기쁜", "긴",
            "깊은", "깎아지른", "깨끗한", "나쁜", "나은", "난데없는", "날랜", "날카로운", "낮은",
            "너그러운", "너른", "널따란", "넓은", "네모난", "노란", "높은", "누런", "눅은", "느닷없는",
            "느린", "늦은", "다른", "더러운", "더운", "덜된", "돈", "동그란", "돼먹잖은", "된",
            "둥그런", "둥근", "뒤늦은", "드문", "딱한", "때늦은", "뛰어난", "뜨거운", "막다른",
            "막중한", "많은", "매운", "먼", "멋진", "메마른", "메스꺼운", "모난", "못난", "못된",
            "못생긴", "무거운", "무딘", "무른", "무서운", "미끈미끈한", "미운", "미친", "바람직스러운",
            "반가운", "밝은", "밤늦은", "보드라운", "보람찬", "보잘것없는", "부드러운", "부른", "붉은",
            "비싼", "빠른", "빨간", "뻘건", "뼈저린", "뽀얀", "뿌연", "새로운", "서툰", "섣부른",
            "설운", "성가신", "센", "수다스러운", "수줍은", "쉬운", "스스러운", "슬픈", "시원찮은", "싫은",
            "싼", "쌀쌀맞은", "쏜살같은", "쓰디쓴", "쓰린", "쓴", "아니꼬운", "아닌", "아름다운",
            "아쉬운", "아픈", "안된", "안쓰러운", "안타까운", "않은", "알맞은", "약빠른", "약은", "얇은",
            "얕은", "어두운", "어려운", "어린", "언짢은", "엄청난", "없는", "여문", "열띤", "예쁜",
            "올바른", "옳은", "외로운", "우스운", "의심쩍은", "이른", "익은", "있는", "작은",
            "잘난", "잘빠진", "재미있는", "적은", "젊은", "점잖은", "조그만", "좁은", "좋은", "주제넘은",
            "줄기찬", "즐거운", "지나친", "지혜로운", "질긴", "짓궂은", "짙은", "짠", "짧은",
            "케케묵은", "큰", "탐스러운", "턱없는", "푸른", "하나같은", "한결같은",
            "흐린", "희망찬", "흰", "힘겨운", "힘찬"
    );

    final List<String> COLORS = Arrays.asList("빨간색", "주황색", "노랑색", "초록색", "파란색", "남색", "보라색");

    final List<String> ANIMALS = Arrays.asList(
            "사자", "호랑이", "코끼리", "기린", "코뿔소", "늑대", "북극곰", "판다", "오랑우탄", "침팬지",
            "원숭이", "고릴라", "초원 개", "사막 여우", "치타", "카멜레온", "하이에나", "코알라", "무지개물범", "하마",
            "바다거북", "아나콘다", "코브라", "투뿔피치", "오리너구리", "피라냐", "독수리", "앵무새", "펭귄", "달팽이",
            "거북이", "해마", "고래", "상어", "연어", "캥거루", "물범", "랫서팬더", "바다사자", "바다거미", "오징어",
            "전갈", "메뚜기", "왕벌", "불가사리", "모래벌레", "왕관해태", "갈매기", "햄스터", "수달", "바다코끼리",
            "팔랑귀", "라마", "비버", "친칠라", "두더지", "박쥐", "금강초원쇠오리", "오랑우퍼", "오로라곰", "하늘다람쥐",
            "올빼미", "저빌", "백조", "독수리", "바다표범", "강아지", "불곰", "호랑지빠귀", "참새", "해마",
            "오소리", "기러기", "쥐", "토끼", "닭", "돼지", "소", "말", "호랑나비", "파리",
            "모기", "개미", "파충류", "척추동물", "무척추동물", "온갖물고기", "해양생물", "포유류", "곤충", "조류"
    );

    public String generate() {
        String nickname = null;
        do {
            String adjective = ADJECTIVES.get(random.nextInt(ADJECTIVES.size()));
            String color = COLORS.get(random.nextInt(COLORS.size()));
            String animal = ANIMALS.get(random.nextInt(ANIMALS.size()));
            StringBuilder stringBuilder = new StringBuilder();
            nickname = stringBuilder.append(adjective).append(SPACE).append(color).append(SPACE).append(animal)
                    .toString();
        } while (memberRepository.existsByNickname(nickname));
        return nickname;
    }
}
