# 알고리즘 스터디

SSAFY 대비 알고리즘 학습 (2026.02 ~)

## 🎯 목표

- 매일 꾸준히 알고리즘 문제 풀이
- AI 코드 리뷰로 실력 향상

## 📁 폴더 구조

```
AlgorithmStudy/
├── .github/
│   └── workflows/
│       ├── daily-folder.yml      # 매일 오전 폴더 생성
│       ├── auto-review.yml       # Push 시 자동 리뷰
│       ├── daily-summary.yml     # 매일 자정 요약
│       └── weekly-retrospect.yml # 주간 회고 Issue
├── 202602/          # 2026년 2월
│   └── 27/          # 2월 27일
│       ├── BOJ_1920.java
│       ├── _review.md
│       └── _optimized.md
├── 202603/          # 2026년 3월
│   └── 01/
├── summaries/       # 일일 학습 요약
└── scripts/         # AI 자동화 스크립트
    ├── all.py            # 리뷰 + 최적화 한번에 ⭐
    ├── init.py           # 날짜 폴더 생성
    ├── review.py         # 코드 리뷰
    ├── optimize.py       # 최적화 제안
    ├── daily_summary.py  # 일일 요약
    └── weekly_summary.py # 주간 회고
```

## 🛠️ 사용법

### 🚀 빠른 시작 (GitHub Actions 사용 시) ⭐ 권장

```bash
# 1. 오늘 폴더는 매일 오전 9시 자동 생성됨!
git pull  # 최신 상태로 동기화

# 2. 문제 풀이 (Java, Python 등)
# 202602/27/BOJ_1920.java 작성

# 3. 커밋 & 푸시만 하면 끝!
git add .
git commit -m "BOJ 1920 풀이"
git push
# → 자동으로 리뷰 + 최적화 실행! 🎉
```

### 🔧 로컬 전용 사용 (GitHub Actions 미사용)

```bash
# 1. 오늘 날짜 폴더 생성
python scripts/init.py

# 2. 문제 풀이 (Java, Python 등)
# 202602/27/BOJ_1920.java 작성

# 3. 리뷰 + 최적화 한번에 실행
python scripts/all.py 202602/27/BOJ_1920.java
```

### 📚 상세 사용법

#### 1. 환경 설정

```bash
# 패키지 설치
pip install -r requirements.txt

# API 키 설정
export GEMINI_API_KEY="your_api_key"
```

#### 2. 코드 작성 & 분석

```bash
# 문제 풀이
# 202602/27/BOJ_1920.java 작성

# 리뷰 + 최적화 한번에
python scripts/all.py 202602/27/BOJ_1920.java

# → _review.md, _optimized.md 생성
```

#### 3. 개별 실행 (선택)

```bash
# 리뷰만
python scripts/review.py 202602/27/BOJ_1920.java

# 최적화만
python scripts/optimize.py 202602/27/BOJ_1920.java
```

## 🤖 AI 자동화

- **Gemini 2.5 Flash** 사용
- 리뷰 + 최적화 한번에 실행 가능
- Python, Java 등 모든 언어 지원
- 결과는 같은 폴더에 마크다운으로 저장

## � GitHub Actions 자동화

### 📁 매일 오전 9시 폴더 자동 생성 ⭐ NEW!

매일 오전 9시(KST)에 자동으로:

- 오늘 날짜 폴더 생성 (예: `202602/28/`)
- README.md 생성 (오늘 날짜 + 응원 메시지)
- **폴더 만들 필요 없이 바로 코드 작성 가능!**

### ⚡ Push 시 자동 리뷰

코드를 push하면 GitHub Actions가 자동으로:

1. 변경된 파일 감지
2. AI 리뷰 + 최적화 실행
3. `_review.md`, `_optimized.md` 자동 생성 및 커밋

### 📊 매일 자정 학습 요약

매일 자정에 자동으로:

- 오늘 푼 문제들 분석
- `summaries/YYYYMMDD.md` 생성

### 📈 주간 회고 (일요일)

매주 일요일마다:

- 이번 주 통계 및 회고 생성
- GitHub Issue로 자동 등록

### 🔑 설정 방법

1. GitHub 레포지토리 → **Settings** → **Secrets and variables** → **Actions**
2. **New repository secret** 클릭
3. Name: `GEMINI_API_KEY`, Value: 본인의 API 키
4. 완료! 이제 push만 하면 자동 실행 🎉

## �📝 파일 명명 규칙

- 백준: `BOJ_문제번호.java` (예: BOJ_1920.java)
- SWEA: `SWEA_문제번호.java` (예: SWEA_1234.java)
- 프로그래머스: `Prog_문제명.java`
- Python도 지원: `boj_1920.py`
