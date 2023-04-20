import json
import random
from datetime import datetime, timedelta

def random_string(length):
    letters = "abcdefghijklmnopqrstuvwxyz"
    return "".join(random.choice(letters) for _ in range(length))

def generate_random_reply(post_id):
    return {
        "Post ID": post_id,
        "Reply Content": f"{random_string(20).capitalize()} {random_string(15).capitalize()} {random_string(30).capitalize()}",
        "Reply Stars": random.randint(1, 5),
        "Reply Author": random_string(8),
        "Secondary Reply Content": f"{random_string(20).capitalize()} {random_string(15).capitalize()} {random_string(30).capitalize()}",
        "Secondary Reply Stars": random.randint(1, 5),
        "Secondary Reply Author": random_string(8),
    }

def main():
    start_id = 203
    end_id = 2001

    with open("replies.json", "r", encoding="utf-8") as f:
        original_data = json.load(f)

    new_data = [generate_random_reply(post_id) for post_id in range(start_id, end_id)]
    original_data.extend(new_data)

    with open("replies_expanded.json", "w", encoding="utf-8") as f:
        json.dump(original_data, f, ensure_ascii=False, indent=4)

if __name__ == "__main__":
    main()
