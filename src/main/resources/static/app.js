// ----------------------------
// app.js
// ----------------------------
console.log("app.js loaded");

// ----------------------------
// User Authentication
// ----------------------------
document.getElementById("signupForm")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const data = {
        name: document.getElementById("signupName").value,
        email: document.getElementById("signupEmail").value,
        password: document.getElementById("signupPassword").value
    };

    try {
        const res = await fetch("/users/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });
        if (res.ok) {
            alert("User registered successfully!");
            window.location.href = "login.html";
        } else {
            const error = await res.json();
            alert(error.message || "Registration failed");
        }
    } catch (err) {
        alert("Error registering user");
        console.error(err);
    }
});

document.getElementById("loginForm")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const data = {
        email: document.getElementById("loginEmail").value,
        password: document.getElementById("loginPassword").value
    };
    try {
        const res = await fetch("/users/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });
        if (res.ok) {
            const user = await res.json();
            localStorage.setItem("userId", user.id);
            localStorage.setItem("userName", user.name);
            alert("Login successful!");
            window.location.href = "index.html";
        } else {
            const error = await res.json();
            alert(error.message || "Invalid email or password");
        }
    } catch (err) {
        alert("Error logging in");
        console.error(err);
    }
});

// ----------------------------
// Events
// ----------------------------
async function getAllEvents() {
    console.log("getAllEvents() called");
    try {
        const res = await fetch("/events");
        if (!res.ok) throw new Error(`Failed to fetch events. Status: ${res.status}`);
        const events = await res.json();
        const container = document.getElementById("eventsContainer");
        if (!container) return;

        container.innerHTML = ""; // clear previous

        if (!events || events.length === 0) {
            container.innerHTML = '<p class="text-gray-400 text-center col-span-full">No events found.</p>';
            return;
        }

        events.forEach((event) => {
            const imageSrc = event.imageUrl || '/uploads/base.jpg';
            const fallbackImage = '/uploads/base.jpg';

            const card = document.createElement("div");
            card.className = "rounded-2xl p-4 glass hover:scale-105 transition cursor-pointer event-card";
            card.innerHTML = `
                <img src="${imageSrc}"
                     class="w-full h-48 object-cover rounded-lg mb-4"
                     alt="event"
                     onerror="if(this.src!=='${fallbackImage}') this.src='${fallbackImage}'">
                <h3 class="text-xl font-semibold mb-2">${event.name || 'Unnamed Event'}</h3>
                <p class="text-gray-300 mb-1">${event.localDate ? new Date(event.localDate).toDateString() : 'TBD'} • ${event.location || 'TBD'}</p>
                <p class="text-gray-300 mb-1">Available Seats: ${event.availableSeats || 0}</p>
                <p class="text-gray-200 font-semibold">Price: ₹${event.price || 0}</p>
            `;

            card.addEventListener("click", () => {
                localStorage.setItem("event", JSON.stringify(event));
                window.location.href = "events-details.html";
            });

            container.appendChild(card);
        });
    } catch (err) {
        console.error("Error in getAllEvents:", err);
        const container = document.getElementById("eventsContainer");
        if (container) {
            container.innerHTML = `<p class="text-red-400 text-center col-span-full py-12">Error loading events: ${err.message}</p>`;
        }
    }
}

async function getEventDetails() {
    const urlParts = window.location.pathname.split('/');
    let eventId = urlParts[urlParts.length - 1];

    if (!eventId || isNaN(eventId)) {
        const eventData = JSON.parse(localStorage.getItem("event") || "{}");
        if (!Object.keys(eventData).length) return;
        renderEventDetails(eventData);
        return;
    }

    try {
        const res = await fetch(`/events/${eventId}`);
        if (!res.ok) throw new Error("Failed to fetch event details");
        const eventData = await res.json();
        renderEventDetails(eventData);
    } catch (err) {
        console.error("Error fetching event details:", err);
        alert("Unable to load event details");
    }
}

function renderEventDetails(eventData) {
    const eventImage = document.getElementById("event-image");
    if (eventImage) {
        const imageSrc = eventData.imageUrl || '/uploads/base.jpg';
        const fallbackImage = '/uploads/base.jpg';
        eventImage.src = imageSrc;
        eventImage.onerror = function() {
            if (this.src !== fallbackImage) this.src = fallbackImage;
        };
    }

    const eventName = document.getElementById("event-name");
    if (eventName) eventName.innerText = eventData.name || "Event Name";

    const eventDateLocation = document.getElementById("event-date-location");
    if (eventDateLocation) eventDateLocation.innerText = `${eventData.localDate ? new Date(eventData.localDate).toDateString() : "TBD"} • ${eventData.location || "TBD"}`;

    const eventDescription = document.getElementById("event-description");
    if (eventDescription) eventDescription.innerText = eventData.description || "Event description goes here...";

    const eventPrice = document.getElementById("event-price");
    if (eventPrice) eventPrice.innerText = `₹${eventData.price || 0}`;

    const eventSeatsLeft = document.getElementById("event-seats-left");
    if (eventSeatsLeft) eventSeatsLeft.innerText = eventData.availableSeats || 0;

    const eventOrganizer = document.getElementById("event-organizer");
    if (eventOrganizer) eventOrganizer.innerText = `Organizer: ${eventData.organizer || "TBD"}`;

    const eventVenue = document.getElementById("event-venue");
    if (eventVenue) eventVenue.innerText = eventData.venue || "Venue details";

    const selectSeatsBtn = document.getElementById("select-seats-btn");
    if (selectSeatsBtn) selectSeatsBtn.href = `/events/${eventData.id}/seats`;
}

// ----------------------------
// Booking & Payment
// ----------------------------
async function createBooking(seatsBooked, paymentMethod="UPI") {
    const eventData = JSON.parse(localStorage.getItem("event") || "{}");
    const userId = parseInt(localStorage.getItem("userId"));
    if (!eventData.id || !userId) return alert("Event or user missing");

    const bookingData = {
        userId,
        eventId: eventData.id,
        seatsBooked,
        totalAmount: seatsBooked * eventData.price,
        paymentMethod,
        paymentStatus: false
    };

    try {
        const res = await fetch("/bookings", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(bookingData)
        });
        if (!res.ok) throw new Error("Booking failed");
        const booking = await res.json();
        localStorage.setItem("bookingId", booking.bookingId);
        window.location.href = "payment.html";
    } catch (err) {
        console.error(err);
        alert(err.message);
    }
}

// ----------------------------
// Page Load
// ----------------------------
document.addEventListener("DOMContentLoaded", () => {
    const eventsContainer = document.getElementById("eventsContainer");
    if (eventsContainer) getAllEvents();

    const eventName = document.getElementById("event-name");
    if (eventName) getEventDetails();
});
