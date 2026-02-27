# 알고리즘 스터디

SSAFY 대비 개인 알고리즘 학습 (2026.02 ~)

## 🎯 목표

- 매일 꾸준히 알고리즘 문제 풀이
- 백준 Silver~Gold / SWEA D3~D5
- AI 코드 리뷰로 실력 향상

## 📁 폴더 구조

```
AlgorithmStudy/
├── 202602/          # 2026년 2월
│   ├── 27/          # 2월 27일
│   │   ├── BOJ_1920.java
│   │   ├── _review.md
│   │   └── _optimized.md
│   ├── 28/
│   └── ...
├── 202603/          # 2026년 3월
│   ├── 01/
│   └── ...
└── scripts/         # AI 자동화 스크립트
    ├── init.py      # 오늘 날짜 폴더 생성
    ├── all.py       # 리뷰 + 최적화 한번에
    ├── analyze.py   # 문제 분석
    ├── review.py    # 코드 리뷰
    └── optimize.py  # 최적화 제안
```

## 🛠️ 사용법

### 🚀 빠른 시작 (권장)

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

#### 2. 오늘 날짜 폴더 생성

```bash
python scripts/init.py
# → 202602/27/ 폴더 자동 생성
```

#### 3. 문제 분석 (풀기 전)

```bash
python scripts/analyze.py "https://www.acmicpc.net/problem/1234"
# 문제 유형, 힌트, 시간복잡도 목표 등 분석
```

#### 4-A. 🌟 전체 분석 (리뷰 + 최적화 한번에)

```bash
python scripts/all.py 202602/27/BOJ_1920.java
# AI가 리뷰 + 최적화를 모두 실행
# 결과: _review.md, _optimized.md 생성
```

#### 4-B. 개별 실행

```bash
# 코드 리뷰만
python scripts/review.py 202602/27/BOJ_1920.java

# 최적화만
python scripts/optimize.py 202602/27/BOJ_1920.java
```

## 🤖 AI 자동화

- **Gemini 2.5 Flash** 사용
- 리뷰 + 최적화 한번에 실행 가능
- Python, Java 등 모든 언어 지원
- 결과는 같은 폴더에 마크다운으로 저장

## 📝 파일 명명 규칙

- 백준: `BOJ_문제번호.java` (예: BOJ_1920.java)
- SWEA: `SWEA_문제번호.java` (예: SWEA_1234.java)
- 프로그래머스: `Prog_문제명.java`
- Python도 지원: `boj_1920.py`
