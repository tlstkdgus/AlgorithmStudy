from google import genai
import os
import sys

client = genai.Client(api_key=os.environ.get("GEMINI_API_KEY", ""))

def analyze(problem_url):
    print("🔍 문제 분석 중...")
    
    response = client.models.generate_content(
        model='models/gemini-2.5-flash',
        contents=f"""
    백준/SWEA 문제 분석: {problem_url}
    
    다음만 알려줘 (직접 풀이 코드는 X):
    1. 문제 유형 (BFS/DFS/DP/그리디 등)
    2. 핵심 아이디어 (힌트만!)
    3. 시간복잡도 목표
    4. 주의할 엣지 케이스
    5. 유사 문제 추천 2개
    
    간결하게!
    """)
    
    result = response.text
    print(result)
    
    with open("_analysis.md", "w", encoding="utf-8") as f:
        f.write(f"# 문제 분석\n\n**URL**: {problem_url}\n\n{result}")
    
    print("\n✅ _analysis.md 저장 완료")

if __name__ == "__main__":
    if len(sys.argv) > 1:
        analyze(sys.argv[1])
    else:
        url = input("문제 URL: ")
        analyze(url)