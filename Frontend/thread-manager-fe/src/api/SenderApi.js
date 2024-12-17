import axios from "axios";

const BASE_URL = "http://localhost:8082/api/senders";

export const createSenders = async (count, isPriorityChangeable) => {
  try {
    const response = await axios.post(`${BASE_URL}/add-senders`, null, {
      params: { count, isPriorityChangeable },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const startAllSenderThreads = async () => {
  try {
    const response = await axios.post(`${BASE_URL}/start-all-threads`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const stopAllSenderThreads = async () => {
  try {
    const response = await axios.post(`${BASE_URL}/stop-all-threads`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const restartAllSenderThreads = async () => {
  try {
    const response = await axios.post(`${BASE_URL}/restart-all-threads`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const startSenderThread = async (index) => {
  try {
    const response = await axios.post(`${BASE_URL}/start-thread`, null, {
      params: { index },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const stopSenderThread = async (index) => {
  try {
    const response = await axios.post(`${BASE_URL}/stop-thread`, null, {
      params: { index },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const restartSenderThread = async (index) => {
  try {
    const response = await axios.post(`${BASE_URL}/restart-thread`, null, {
      params: { index },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const changeSenderThreadPriority = async (index, priority) => {
  try {
    const response = await axios.post(`${BASE_URL}/change-thread-priority?index=${index}&priority=${priority}`, null);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};


