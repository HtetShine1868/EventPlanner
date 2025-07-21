// src/api/axios.js
import axios from 'axios';

export default axios.create({
    baseURL: 'http://localhost:8080', // Your Spring Boot backend URL
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true, // use this if you're using JWT or cookies
});
