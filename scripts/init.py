"""
오늘 날짜 폴더 자동 생성
"""
from datetime import datetime
import os
import sys

MEMBERS = ["신상현", "김효정", "이원준"]

def init_today():
    today = datetime.now()
    year_month = today.strftime("%Y%m")
    day = today.strftime("%d")
    
    for member in MEMBERS:
        folder_path = os.path.join(member, year_month, day)
        
        if os.path.exists(folder_path):
            print(f"✅ 폴더가 이미 존재합니다: {folder_path}")
        else:
            os.makedirs(folder_path, exist_ok=True)
            print(f"✅ 오늘 폴더 생성 완료: {folder_path}")
        
        print(f"\n📝 위치: {os.path.abspath(folder_path)}")

if __name__ == "__main__":
    init_today()
