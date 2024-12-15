// api/threadsApi.js

import axios from "axios";

const BASE_URL = "http://localhost:8080/api/threads";

export const addSenders = async (count, isPriorityChangeable) => {
  try {
    const response = await axios.post(`${BASE_URL}/add-senders`, null, {
      params: { count, isPriorityChangeable },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const addReceivers = async (count, isPriorityChangeable) => {
  try {
    const response = await axios.post(`${BASE_URL}/add-receivers`, null, {
      params: { count, isPriorityChangeable },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};
