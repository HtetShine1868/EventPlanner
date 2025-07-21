import React, { useEffect, useState } from 'react';
import axios from '../api/axios';

function EventList() {
    const [events, setEvents] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        axios.get('/api/event')
            .then(response => {
                console.log("Fetched response:", response.data);
                // Access events inside 'content' key
                setEvents(response.data.content || []);
            })
            .catch(err => {
                console.error("Error fetching events:", err);
                setError('Failed to fetch events. Please try again.');
            });
    }, []);

    return (
        <div style={{ padding: '20px' }}>
            <h2>Approved Events</h2>

            {error && <p style={{ color: 'red' }}>{error}</p>}

            {events.length === 0 && !error && (
                <p>No approved events available.</p>
            )}

            <ul>
                {events.map(event => (
                    <li key={event.id}>
                        <strong>{event.title}</strong> – {event.location || 'Location TBD'}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default EventList;
