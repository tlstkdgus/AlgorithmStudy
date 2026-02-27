"""
코드 분석 올인원: 리뷰 + 최적화 한번에 실행
"""
from google import genai
import os
import sys

client = genai.Client(api_key=os.environ.get("GEMINI_API_KEY", ""))

def analyze_all(file_path):
    if not os.path.exists(file_path):
        print(f"❌ 파일을 찾을 수 없습니다: {file_path}")
        return
    
    print(f"📂 파일: {file_path}")
    
    with open(file_path, 'r', encoding='utf-8') as f:
        code = f.read()
    
    file_dir = os.path.dirname(file_path) if os.path.dirname(file_path) else "."
    
    # 1. 코드 리뷰
    print("\n🔬 코드 리뷰 중...")
    review_response = client.models.generate_content(
        model='models/gemini-2.5-flash',
        contents=f"""
    내 알고리즘 풀이를 리뷰해줘:
```
    {code}
```
    
    다음을 분석해줘:
    1. 시간/공간 복잡도
    2. 논리 오류 있는지
    3. 더 효율적인 방법
    4. 놓친 엣지 케이스
    5. 코드 개선점
    
    비판적이되 건설적으로!
    """)
    
    review_result = review_response.text
    review_path = os.path.join(file_dir, "_review.md")
    with open(review_path, "w", encoding="utf-8") as f:
        f.write(f"# 코드 리뷰\n\n**파일**: {file_path}\n\n{review_result}")
    print(f"✅ {review_path} 저장 완료")
    
    # 2. 최적화 제안
    print("\n⚡ 최적화 분석 중...")
    optimize_response = client.models.generate_content(
        model='models/gemini-2.5-flash',
        contents=f"""
    다음 알고리즘 코드를 최적화해줘:
```
    {code}
```
    
    다음 내용을 포함해서:
    1. 시간복잡도 개선 방법
    2. 공간복잡도 개선 방법
    3. 가독성 개선
    4. 최종 최적화된 코드
    5. 왜 이 방법이 최선인지 설명
    
    구체적인 코드 변경 예시 포함.
    """)
    
    optimize_result = optimize_response.text
    optimize_path = os.path.join(file_dir, "_optimized.md")
    with open(optimize_path, "w", encoding="utf-8") as f:
        f.write(f"# 최적화 분석\n\n{optimize_result}")
    print(f"✅ {optimize_path} 저장 완료")
    
    print("\n" + "="*50)
    print("✨ 분석 완료!")
    print(f"📄 리뷰: {review_path}")
    print(f"⚡ 최적화: {optimize_path}")

if __name__ == "__main__":
    if len(sys.argv) > 1:
        analyze_all(sys.argv[1])
    else:
        file = input("코드 파일 경로: ")
        analyze_all(file)
