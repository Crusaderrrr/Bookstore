import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,  // Automatically send cookies on all requests
});

// Request interceptor to attach access token header unless skipAuth is set
axiosInstance.interceptors.request.use(
  async (config) => {
    if (!config.skipAuth) {
      const accessToken = localStorage.getItem("accessToken");
      if (accessToken) {
        config.headers["Authorization"] = "Bearer " + accessToken;
      }
    }
    return config; // always return config
  },
  (error) => Promise.reject(error)
);

// Response interceptor to handle 401 errors and refresh tokens
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const response = await axios.post(
          "http://localhost:8080/refresh_token",
          {},
          { withCredentials: true }
        );
        console.log(response.data);
        const newAccessToken = response.data.accessToken;

        localStorage.setItem("accessToken", newAccessToken);

        originalRequest.headers["Authorization"] = "Bearer " + newAccessToken;
        return axiosInstance(originalRequest);
      } catch (refreshError) {
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
