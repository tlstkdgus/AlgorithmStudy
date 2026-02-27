# 🚀 빠른 설정 가이드

## 1️⃣ GitHub Secrets 설정 (필수)

GitHub Actions를 사용하려면 API 키를 등록해야 합니다.

### 단계:

1. GitHub 레포지토리 페이지로 이동
2. **Settings** 탭 클릭
3. 왼쪽 메뉴에서 **Secrets and variables** → **Actions** 클릭
4. **New repository secret** 버튼 클릭
5. 다음 정보 입력:
   - **Name**: `GEMINI_API_KEY`
   - **Secret**: 본인의 Gemini API 키 붙여넣기
6. **Add secret** 클릭

✅ 완료! 이제 push만 하면 자동으로 리뷰가 실행됩니다.

---

## 2️⃣ 로컬 환경 설정

### Python 패키지 설치

```bash
pip install -r requirements.txt
```

### 환경변수 설정

```bash
# Linux/Mac
export GEMINI_API_KEY="your_api_key_here"

# Windows (PowerShell)
$env:GEMINI_API_KEY="your_api_key_here"

# Windows (CMD)
set GEMINI_API_KEY=your_api_key_here
```

---

## 3️⃣ GitHub Actions 워크플로우

### ⚡ auto-review.yml

- **실행 시점**: 코드 push 시
- **동작**: 변경된 파일 자동 리뷰 및 커밋

### 📊 daily-summary.yml

- **실행 시점**: 매일 자정 (KST)
- **동작**: 오늘 푼 문제 요약 생성

### 📈 weekly-retrospect.yml

- **실행 시점**: 매주 일요일 오후 6시 (KST)
- **동작**: 주간 회고 Issue 자동 생성

---

## 4️⃣ 일일 워크플로우

```bash
# 1. 오늘 폴더 생성
python scripts/init.py

# 2. 문제 풀이
# 202602/27/BOJ_1920.java 작성

# 3. Git 커밋 & 푸시
git add .
git commit -m "BOJ 1920 풀이"
git push

# → 자동으로 리뷰 실행! 🎉
# → 약 30초 후 _review.md, _optimized.md 자동 생성
```

---

## 5️⃣ 수동 리뷰 (로컬)

GitHub Actions 없이 로컬에서만 사용하려면:

```bash
# 리뷰 + 최적화 한번에
python scripts/all.py 202602/27/BOJ_1920.java

# 또는 개별 실행
python scripts/review.py 202602/27/BOJ_1920.java
python scripts/optimize.py 202602/27/BOJ_1920.java
```

---

## 🔥 Tip

### GitHub Actions 수동 실행

1. GitHub 레포지토리 → **Actions** 탭
2. 원하는 워크플로우 선택
3. **Run workflow** 클릭

### 일일 요약 수동 생성

```bash
python scripts/daily_summary.py
```

### 주간 회고 수동 생성

```bash
python scripts/weekly_summary.py
```

---

## ❓ 문제 해결

### "No module named 'google.genai'"

```bash
pip install google-genai
```

### "API key not found"

환경변수가 제대로 설정되었는지 확인:

```bash
echo $GEMINI_API_KEY  # Linux/Mac
echo %GEMINI_API_KEY%  # Windows
```

### GitHub Actions가 실행되지 않음

1. Settings → Actions → General → Workflow permissions
2. "Read and write permissions" 선택
3. "Allow GitHub Actions to create and approve pull requests" 체크
4. Save
