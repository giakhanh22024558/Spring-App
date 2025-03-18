import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:9090/api', // Replace with your Spring Boot API base URL
  headers: {
    'Content-Type': 'application/json',
  },
});

export default {
  getHelloMessage() {
    return apiClient.get('/hello'); // Replace with your API endpoint
  },
  // Add more methods as needed
};