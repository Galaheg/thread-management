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

export const startAllThreads = async () => {
  try {
    const response = await axios.post(`${BASE_URL}/start-all-threads`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const stopAllThreads = async () => {
  try {
    const response = await axios.post(`${BASE_URL}/stop-all-threads`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const restartAllThreads = async () => {
  try {
    const response = await axios.post(`${BASE_URL}/restart-all-threads`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const startThread = async (index) => {
  try {
    const response = await axios.post(`${BASE_URL}/start-thread`, null, {
      params: { index },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const stopThread = async (index) => {
  try {
    const response = await axios.post(`${BASE_URL}/stop-thread`, null, {
      params: { index },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const restartThread = async (index) => {
  try {
    const response = await axios.post(`${BASE_URL}/restart-thread`, null, {
      params: { index },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const changeThreadPriority = async (index, priority) => {
  try {
    const response = await axios.post(`${BASE_URL}/change-thread-priority?index=${index}&priority=${priority}`, null);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};


