// OrderTrackingPage.js - handles review popover + interactive star rating

const closeAllPopovers = () => {
  document.querySelectorAll('.review-popover.open').forEach(pop => {
    const btn = pop.previousElementSibling;
    if (btn && btn.classList.contains('open-review-btn')) {
      btn.setAttribute('aria-expanded', 'false');
    }
    pop.classList.remove('open');
    pop.setAttribute('aria-hidden', 'true');
  });
};

const setStarsUI = (container, value) => {
  const stars = Array.from(container.querySelectorAll('.star'));
  stars.forEach(star => {
    const v = Number(star.dataset.value);
    const active = v <= value;
    star.classList.toggle('active', active);
    star.setAttribute('aria-pressed', active ? 'true' : 'false');
  });
};

const initPopoverForItem = (openBtn) => {
  const popover = openBtn.nextElementSibling;
  if (!popover || !popover.classList.contains('review-popover')) return;

  const form = popover.querySelector('form.write-review-form');
  const hiddenRating = form.querySelector('input[name="rating"]');
  const starGroup = form.querySelector('.star-input');
  const stars = Array.from(starGroup.querySelectorAll('.star'));
  const closeBtn = form.querySelector('.close-popover');

  // Toggle open
  openBtn.addEventListener('click', (e) => {
    e.stopPropagation();
    const isOpen = popover.classList.contains('open');
    closeAllPopovers();
    if (!isOpen) {
      popover.classList.add('open');
      popover.setAttribute('aria-hidden', 'false');
      openBtn.setAttribute('aria-expanded', 'true');
      // reset UI state (keep previous selection if any)
      const currentVal = Number(hiddenRating.value || 0);
      setStarsUI(starGroup, currentVal);
    } else {
      openBtn.setAttribute('aria-expanded', 'false');
    }
  });

  // Star click select
  stars.forEach((star) => {
    star.addEventListener('click', (e) => {
      e.preventDefault();
      e.stopPropagation();
      const val = Number(star.dataset.value);
      hiddenRating.value = String(val);
      setStarsUI(starGroup, val);
      // clear potential invalid style
      starGroup.classList.remove('invalid');
      starGroup.removeAttribute('aria-invalid');
    });

    // Hover preview
    star.addEventListener('mouseenter', () => {
      const val = Number(star.dataset.value);
      setStarsUI(starGroup, val);
    });

    star.addEventListener('mouseleave', () => {
      const currentVal = Number(hiddenRating.value || 0);
      setStarsUI(starGroup, currentVal);
    });
  });

  // Close actions
  if (closeBtn) {
    closeBtn.addEventListener('click', (e) => {
      e.preventDefault();
      e.stopPropagation();
      popover.classList.remove('open');
      popover.setAttribute('aria-hidden', 'true');
      openBtn.setAttribute('aria-expanded', 'false');
    });
  }

  // Validate before submit
  form.addEventListener('submit', (e) => {
    if (!hiddenRating.value) {
      e.preventDefault();
      starGroup.classList.add('invalid');
      starGroup.setAttribute('aria-invalid', 'true');
      // Optional: simple alert; replace by toast if available
      try { window.toast && window.toast.error && window.toast.error('Vui lòng chọn số sao'); } catch(_) { alert('Vui lòng chọn số sao'); }
    }
  });
};

// Close on outside click and Escape
const installGlobalClosers = () => {
  document.addEventListener('click', (e) => {
    const isInside = e.target.closest('.review-popover') || e.target.closest('.open-review-btn');
    if (!isInside) {
      closeAllPopovers();
    }
  });
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') closeAllPopovers();
  });
};

const showRedirectErrorIfAny = () => {
  try {
    const params = new URLSearchParams(window.location.search);
    const msg = params.get('error');
    if (msg) {
      if (window.toast && window.toast.error) window.toast.error(msg);
      else alert(msg);
    }
  } catch (_) { /* ignore */ }
};

// Init on DOM ready
window.addEventListener('DOMContentLoaded', () => {
  const openButtons = document.querySelectorAll('.open-review-btn');
  openButtons.forEach(initPopoverForItem);
  installGlobalClosers();
  showRedirectErrorIfAny();
});
