import json
import random
from datetime import datetime, timedelta


def random_string(length):
    letters = "abcdefghijklmnopqrstuvwxyz"
    return "".join(random.choice(letters) for _ in range(length))


def random_date(start_date, end_date):
    delta = end_date - start_date
    random_days = random.randrange(delta.days)
    return start_date + timedelta(days=random_days)


def generate_random_data(start_id, end_id):
    data = []
    start_date = datetime(2000, 1, 1)
    end_date = datetime(2021, 9, 1)

    for post_id in range(start_id, end_id):
        post = {
            "PostID": post_id,
            "Title": f"{random_string(5).capitalize()} {random_string(7).capitalize()} {random_string(6).capitalize()}",
            "Category": [random_string(6).capitalize() for _ in range(random.randint(1, 4))],
            "Content": f"{random_string(20).capitalize()} {random_string(15).capitalize()} {random_string(30).capitalize()}",
            "PostingTime": str(random_date(start_date, end_date)),
            "PostingCity": f"{random_string(8).capitalize()}, {random_string(7).capitalize()}",
            "Author": random_string(8),
            "AuthorRegistrationTime": str(random_date(start_date, end_date)),
            "AuthorID": str(random.randint(100000000000000000, 999999999999999999)),
            "AuthorPhone": str(random.randint(10000000000, 99999999999)),
            "AuthorFollowedBy": [random_string(8) for _ in range(random.randint(1, 10))],
            "AuthorFavorite": [random_string(8) for _ in range(random.randint(1, 45))],
            "AuthorShared": [random_string(8) for _ in range(random.randint(1, 45))],
            "AuthorLiked": [random_string(8) for _ in range(random.randint(1, 50))],
        }
        data.append(post)
    return data


def main():
    start_id = 203
    end_id = 2041

    with open("posts.json", "r", encoding="utf-8") as f:
        original_data = json.load(f)

    new_data = generate_random_data(start_id, end_id)
    original_data.extend(new_data)

    with open("posts_10times.json", "w", encoding="utf-8") as f:
        json.dump(original_data, f, ensure_ascii=False, indent=4)


if __name__ == "__main__":
    main()
