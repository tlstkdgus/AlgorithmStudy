"""
매일 자정 오늘 푼 문제 요약
"""
from google import genai
from datetime import datetime
import os
import glob

client = genai.Client(api_key=os.environ.get("GEMINI_API_KEY", ""))

MEMBERS = ["신상현", "김효정", "이원준"]

def generate_daily_summary_for(member, today):
    today_path = f"{member}/{today.strftime('%Y%m')}/{today.strftime('%d')}"
    
    # 오늘 푼 문제들 찾기
    files = []
    if os.path.exists(today_path):
        files = glob.glob(f"{today_path}/*.py") + \
                glob.glob(f"{today_path}/*.java") + \
                glob.glob(f"{today_path}/*.cpp") + \
                glob.glob(f"{today_path}/*.js")
    
    if not files:
        print(f"📭 [{member}] {today.strftime('%Y년 %m월 %d일')} - 오늘 푼 문제가 없습니다.")
        return
    
    summary_text = f"# 📚 [{member}] {today.strftime('%Y년 %m월 %d일')} 학습 요약\n\n"
    summary_text += f"## 풀이한 문제 ({len(files)}개)\n\n"
    
    for file in files:
        try:
            with open(file, 'r', encoding='utf-8') as f:
                code = f.read()
            
            # AI로 간단 요약
            response = client.models.generate_content(
                model='models/gemini-2.5-flash',
                contents=f"""
다음 코드를 한 줄로 요약해줘:
```
{code[:500]}  # 처음 500자만
```

형식: "알고리즘 유형 - 핵심 로직 - 시간복잡도"
예: "이진탐색 - 정렬 후 탐색 - O(n log n)"
"""
            )
            
            summary_text += f"### {os.path.basename(file)}\n"
            summary_text += f"{response.text}\n\n"
        except Exception as e:
            print(f"⚠️ {file} 처리 중 오류: {e}")
            summary_text += f"### {os.path.basename(file)}\n"
            summary_text += f"처리 실패\n\n"
    
    # 저장
    summary_dir = os.path.join(member, "summaries")
    os.makedirs(summary_dir, exist_ok=True)
    summary_file = f"{summary_dir}/{today.strftime('%Y%m%d')}.md"
    with open(summary_file, "w", encoding="utf-8") as f:
        f.write(summary_text)
    
    print(summary_text)
    print(f"\n✅ 요약 저장: {summary_file}")

def generate_daily_summary():
    today = datetime.now()
    for member in MEMBERS:
        generate_daily_summary_for(member, today)

if __name__ == "__main__":
    generate_daily_summary()
