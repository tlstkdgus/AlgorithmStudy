"""
주간 회고 생성
"""
from google import genai
from datetime import datetime, timedelta
import os
import glob

client = genai.Client(api_key=os.environ.get("GEMINI_API_KEY", ""))

MEMBERS = ["신상현", "김효정", "이원준"]

def generate_weekly_summary_for(member, today):
    week_ago = today - timedelta(days=7)
    
    # 이번 주 푼 문제들 찾기
    all_files = []
    for i in range(7):
        day = week_ago + timedelta(days=i)
        day_path = f"{member}/{day.strftime('%Y%m')}/{day.strftime('%d')}"
        if os.path.exists(day_path):
            all_files.extend(glob.glob(f"{day_path}/*.py"))
            all_files.extend(glob.glob(f"{day_path}/*.java"))
            all_files.extend(glob.glob(f"{day_path}/*.cpp"))
            all_files.extend(glob.glob(f"{day_path}/*.js"))
    
    if not all_files:
        print(f"[{member}] 이번 주 푼 문제가 없습니다.")
        return
    
    # 언어별 분류
    languages = {}
    for file in all_files:
        ext = os.path.splitext(file)[1]
        languages[ext] = languages.get(ext, 0) + 1
    
    files_info = "\n".join([f"- {os.path.basename(f)}" for f in all_files])
    
    # AI로 주간 요약
    response = client.models.generate_content(
        model='models/gemini-2.5-flash',
        contents=f"""
이번 주 알고리즘 학습 회고를 작성해줘:

📊 통계:
- 총 문제 수: {len(all_files)}개
- 사용 언어: {', '.join([f'{k}({v}개)' for k, v in languages.items()])}

풀이한 문제들:
{files_info}

다음 형식으로 동기부여되는 톤으로 작성:

## 🌟 이번 주 하이라이트
(인상 깊었던 문제나 성장 포인트)

## 💡 배운 점
(새로 알게 된 알고리즘이나 개선점)

## 🎯 다음 주 목표
(도전할 알고리즘 유형이나 목표)

## 💪 한마디
(격려와 동기부여)
"""
    )
    
    summary = f"""# 📊 [{member}] Week {today.isocalendar()[1]} 회고

**기간**: {week_ago.strftime('%Y.%m.%d')} ~ {today.strftime('%Y.%m.%d')}

## 📈 통계
- 총 문제 수: **{len(all_files)}개**
- 사용 언어: {', '.join([f'**{k}**({v}개)' for k, v in languages.items()])}

{response.text}
"""
    
    print(summary)
    return summary

def generate_weekly_summary():
    today = datetime.now()
    all_summaries = []
    for member in MEMBERS:
        summary = generate_weekly_summary_for(member, today)
        if summary is not None:
            all_summaries.append(summary)
    
    if all_summaries:
        print("\n" + "="*50)
        print("\n".join(all_summaries))

if __name__ == "__main__":
    generate_weekly_summary()
