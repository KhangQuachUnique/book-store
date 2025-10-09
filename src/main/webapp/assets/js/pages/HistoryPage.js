// assets/js/pages/HistoryPage.js
// View History page script
// - Lazy-load thumbnails for smoother UX
// - showResult() uses global showToast if available (same pattern with other pages)

function showResult(message, success = true, duration = 3000) {
  // Optional: align with existing showToast if it exists
  try {
    if (typeof showToast === "function") {
      showToast(message, success ? "success" : "error", duration);
      return;
    }
  } catch (_) {}
  // Fallback: console
  console[success ? "log" : "warn"](message);
}

function lazyLoadImages(root = document) {
  const imgs = root.querySelectorAll(".view-history img[data-src]");
  if (!imgs.length) return;

  if ("IntersectionObserver" in window) {
    const io = new IntersectionObserver((entries, obs) => {
      entries.forEach((e) => {
        if (!e.isIntersecting) return;
        const img = e.target;
        img.src = img.dataset.src;
        img.removeAttribute("data-src");
        obs.unobserve(img);
      });
    }, { rootMargin: "100px" });

    imgs.forEach((img) => io.observe(img));
  } else {
    // Fallback without IO
    imgs.forEach((img) => {
      if (img.dataset.src) {
        img.src = img.dataset.src;
        img.removeAttribute("data-src");
      }
    });
  }
}

// Optional hook if later you add a "Clear history" or "Load more" button
function bindActions(root = document) {
  // Example (kept as placeholder):
  // const clearBtn = root.querySelector("[data-action='clear-history']");
  // if (clearBtn) {
  //   clearBtn.addEventListener("click", async () => {
  //     try {
  //       // await fetch(..., { method: "POST" })
  //       showResult("Cleared view history.");
  //       // Optionally re-render / reload
  //       location.reload();
  //     } catch (e) {
  //       showResult("Failed to clear history.", false);
  //     }
  //   });
  // }
}

document.addEventListener("DOMContentLoaded", () => {
  // Initialize page behaviors
  lazyLoadImages(document);
  bindActions(document);
});
