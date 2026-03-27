
const tweetBox = document.querySelector(".tweet-box");
const tweetBtn = document.querySelector(".tweet-btn");
const postsEl = document.querySelector(".posts");
const floatingBtn = document.querySelector(".floating-tweet-box-icon");
const tweetText = document.querySelector(".tweet-text");

function rand(min, max) {
  return Math.floor(Math.random()*(max-min+1)) + min;
}

function isMobile() {
  return window.matchMedia("(max-width: 520px)").matches;
}

function syncTweetBoxVisibility() {
  if (isMobile()) tweetBox.classList.add("is-hidden");
  else tweetBox.classList.remove("is-hidden");
}

window.addEventListener("resize",syncTweetBoxVisibility);
syncTweetBoxVisibility();

function createPostElement({
  text,
  mediaType = null,
  mediaSrc = "",
  likes = 0,
  retweets = 0,
  comments = 0
}) {
  const post = document.createElement("article");
  post.className = "post";
  post.dataset.liked = "false";
  post.dataset.retweeted = "false";

  const mediaHtml =
    mediaType === "image"
      ? `<img class="post-image" src="${mediaSrc}" alt="" />`
      : mediaType === "video"
      ? `<video class="post-video" src="${mediaSrc}" controls></video>`
      : "";

  post.innerHTML = `
    <div class="post-body">
      <div class="post-text">${highlightHashtags(text)}</div>
      <div class="post-media">${mediaHtml}</div>

      <div class="post-actions">
        <button class="comment-btn" type="button" aria-label="Comment">
          <img class="action-icon" src="assets/icons/comment.svg" alt="Comment" />
          <span class="comment-count">${comments}</span>
        </button>

        <button class="retweet-btn" type="button" aria-label="Retweet">
          <img class="action-icon" src="assets/icons/retweet.svg" alt="Retweet" />
          <span class="retweet-count">${retweets}</span>
        </button>

        <button class="like-post" type="button" aria-label="Like">
          <img class="action-icon like-icon" src="assets/icons/like.svg" alt="Like" />
          <span class="likes-count">${likes}</span>
        </button>
      </div>

      <div class="comments">
        <div class="comment-box">
          <input class="comment-input" type="text" placeholder="Write a comment..." />
          <button class="comment-submit" type="button">Reply</button>
        </div>
        <div class="comment-list"></div>
      </div>
    </div>
  `;

  return post;
}

function seedDefaults() {
  const p1 = createPostElement({
    text: "Text-only default post",
    mediaType: null,
    mediaSrc: "",
    likes: rand(1, 300),
    retweets: rand(0, 120),
    comments: rand(0, 60)
  });

  const p2 = createPostElement({
    text: "Image default post",
    mediaType: "image",
    mediaSrc: "assets/icons/post-image.png",
    likes: rand(1, 300),
    retweets: rand(0, 120),
    comments: rand(0, 60)
  });

  const p3 = createPostElement({
    text: "Video default post",
    mediaType: "video",
    mediaSrc: "assets/icons/video.mp4",
    likes: rand(1, 300),
    retweets: rand(0, 120),
    comments: rand(0, 60)
  });

  postsEl.append(p1, p2, p3);
}

function addNewPost(text){
  const post = createPostElement({
    text,
    mediaType: null,
    mediaSrc: "",
    likes: 0,
    retweets: 0,
    comments: 0
  });

  postsEl.prepend(post);
}

tweetBtn.addEventListener("click",()=>{
  const text = tweetText.value.trim();
  if (!text) return;
  addNewPost(text);
  tweetText.value = "";
  if (isMobile()) tweetBox.classList.add("is-hidden");
});

floatingBtn.addEventListener("click", () => {
  if (!isMobile()) return;
  tweetBox.classList.remove("is-hidden");
  tweetText.focus();
});

postsEl.addEventListener("click",(e)=>{
  const likeBtn = e.target.closest(".like-post");
  const rtBtn = e.target.closest(".retweet-btn");
  const commentBtn = e.target.closest(".comment-btn");
  const submitBtn = e.target.closest(".comment-submit");

  if (likeBtn) {
    const post = likeBtn.closest(".post");
    const countEl = likeBtn.querySelector(".likes-count");
    const iconEl = likeBtn.querySelector(".like-icon");
    let count = Number(countEl.textContent);
    if (Number.isNaN(count)) count = 0;

    const liked = post.dataset.liked === "true";
    if (!liked) {
      post.dataset.liked = "true";
      likeBtn.classList.add("unlike-post");
      countEl.textContent = String(count + 1);
      if (iconEl) iconEl.src = "assets/icons/like-pink.svg";
    } else {
      post.dataset.liked = "false";
      likeBtn.classList.remove("unlike-post");
      countEl.textContent = String(Math.max(0, count - 1));
      if (iconEl) iconEl.src = "assets/icons/like.svg";
    }
    return;
  }

  if (rtBtn) {
    const post = rtBtn.closest(".post");
    const countEl = rtBtn.querySelector(".retweet-count");
    let count = Number(countEl.textContent);
    if (Number.isNaN(count)) count = 0;

    const on = post.dataset.retweeted === "true";
    if (!on) {
      post.dataset.retweeted = "true";
      countEl.textContent = String(count + 1);
    } else {
      post.dataset.retweeted = "false";
      countEl.textContent = String(Math.max(0, count - 1));
    }
    return;
  }

  if (commentBtn) {
    const post = commentBtn.closest(".post");
    const box = post.querySelector(".comment-box");
    const input = post.querySelector(".comment-input");
    if (box) box.classList.toggle("is-open");
    if (input) input.focus();
    return;
  }

  if (submitBtn) {
    const post = submitBtn.closest(".post");
    const input = post.querySelector(".comment-input");
    const list = post.querySelector(".comment-list");
    const countEl = post.querySelector(".comment-count");

    const val = input ? input.value.trim() : "";
    if (!val) return;

    const item = document.createElement("div");
    item.className = "comment";
    item.textContent = val;
    if (list) list.prepend(item);
    if (input) input.value = "";

    let count = Number(countEl.textContent);
    if (Number.isNaN(count)) count = 0;
    countEl.textContent = String(count + 1);
  }
});


const backBtn = document.querySelector(".composer-back");

if (backBtn) {
  backBtn.addEventListener("click", () => {
    if (isMobile()) tweetBox.classList.add("is-hidden");
  });
}
 
function highlightHashtags(text) {
  return text.replace(/#(\w+)/g, '<span class="hashtag">#$1</span>');
}

seedDefaults();
