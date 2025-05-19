# Ứng dụng doanh nghiệp ERP Spring-boot đơn giản 

## 📊 Tổng Quan

Tài liệu này mô tả kiến trúc hiện tại của cụm GKE được triển khai với Cloudflare tunnel để xuất các dịch vụ nội bộ ra ngoài, tập trung vào các dịch vụ Backend, Frontend và Keycloak.

---

## 🏗️ Kiến Trúc Tổng Thể

![PTUDDN-Workflow drawio](https://github.com/user-attachments/assets/ac5c0d2f-4a08-44dc-9e0a-9cd3991e533f)

Dưới đây là mô tả kiến trúc tổng thể của hệ thống SIMPLE ERP SPRING APP theo sơ đồ bạn cung cấp:

## 1️⃣ Web Service Layer (Lớp Dịch vụ web)

* **Keycloak**: Quản lý xác thực và phân quyền người dùng (IAM).
* **Vue.js app**: Ứng dụng giao diện người dùng (frontend) của hệ thống ERP.
* Người dùng sẽ truy cập thông qua domain: `https://isduckduck.me2uet.io.vn`.

---

## 2️⃣ CI/CD Pipeline (Tích hợp & Triển khai liên tục)

* **Spring-app Repository (GitHub)**:

  * Dev (Dev1, Dev2, Dev3) thực hiện commit, merge code.
  * Kích hoạt pipeline tự động với **GitAction CI**.
  * Các bước: **Test, Build, Push Docker images**.
* **DockerHub Registry**:

  * Lưu trữ các Docker images sau khi build thành công.
* **k8s manifest repository**:

  * Lưu giữ file cấu hình Kubernetes (YAML).
  * Cập nhật image tag mới, commit manifest mới.
* **ArgoCD**:

  * Đồng bộ các manifest mới từ repository về cluster.
  * Triển khai ứng dụng mới lên Kubernetes cluster tự động (CD).

---

## 3️⃣ GKE Cluster (Hạ tầng vận hành trên Google Kubernetes Engine)

### Cloudflare Protection:

* **Cloudflare network**: Lớp bảo vệ đầu vào, chặn DDoS, CDN, SSL termination.
* **Cloudflare Tunnel**:

  * Proxy an toàn giúp kết nối từ Cloudflare tới Kubernetes mà không cần public IP.
  * Được triển khai dưới dạng pod ở **Node 2**.
  * Chuyển tiếp traffic đến frontend service qua Internal ClusterIP.

### Kubernetes Services Layer:

* Các dịch vụ (ClusterIP) dùng để route nội bộ:

  * **Frontend Service**
  * **Backend Service**
  * **Keycloak Service**
  * Kết nối với nhau thông qua API Gateway nội bộ.

### Application Pods Layer:

* **Node 1**:

  * Backend pod.
  * Keycloak pod.
* **Node 2**:

  * Frontend pod.

### Persistent Storage Layer:

* **PVC (Persistent Volume Claim)**:

  * Request dung lượng lưu trữ từ PV cho ứng dụng sử dụng.
* **PV (Persistent Volume)**:

  * Vùng lưu trữ vật lý cố định.
* **MySQL Pods**:

  * Node 1:

    * MySQL phục vụ cho Keycloak.
  * Node 1 (có thể shared):

    * MySQL phục vụ cho ứng dụng backend.

---

## 🔄 Luồng hoạt động tổng thể:

1. Người dùng truy cập hệ thống thông qua domain Cloudflare.
2. Cloudflare tunnel đưa traffic tới frontend pod.
3. Frontend gọi API tới backend, backend có thể xác thực thông qua Keycloak.
4. Dữ liệu backend, keycloak sử dụng MySQL làm database.
5. Developer phát triển tính năng, đẩy code lên GitHub.
6. GitAction tự động build docker images và deploy lên cluster thông qua ArgoCD.
7. Toàn bộ hạ tầng được vận hành trên Google Kubernetes Engine (GKE).

---

## 🧱 Cấu Trúc Tầng

![PTUDDN-Structure drawio](https://github.com/user-attachments/assets/5ffb68dd-7072-4c56-b355-724a49ce1acf)

## 1️⃣ Visit Layer (Lớp Truy Cập)

* **WEB (Vue.js)**: Giao diện người dùng, ứng dụng frontend được xây dựng bằng Vue.js, là điểm tiếp xúc trực tiếp với người dùng cuối.

## 2️⃣ Cloudflare Access Layer (External Ingress Layer)

* **Cloudflare Network**: Lớp bảo vệ bên ngoài giúp lọc traffic, chặn DDoS, tối ưu CDN, SSL termination và chuyển tiếp request vào hệ thống nội bộ một cách an toàn.

## 3️⃣ Cloudflare Tunnel Layer (Ingress Proxy Layer)

* **Cloudflare Tunnel Pod**: Đóng vai trò là proxy bảo mật, kết nối từ Cloudflare Network đến các dịch vụ nội bộ của Kubernetes Cluster mà không cần public IP.

## 4️⃣ Service Mesh / ClusterIP Layer (Internal Service Routing)

* **Frontend Service (ClusterIP)**: Cung cấp route nội bộ tới frontend pod.
* **Backend Service (ClusterIP)**: Định tuyến các request API tới backend pod.
* **Keycloak Service (ClusterIP)**: Quản lý xác thực, phân quyền người dùng, route nội bộ tới keycloak pod.

## 5️⃣ Application Layer (Workloads Layer)

* **Frontend Pod (Node 2)**: Pod triển khai giao diện người dùng.
* **Backend Pod (Node 1)**: Pod xử lý logic nghiệp vụ của hệ thống ERP.
* **Keycloak Pod (Node 1)**: Pod triển khai dịch vụ IAM (Identity & Access Management).

## 6️⃣ Database Layer (Persistent Storage Layer)

* **MySQL for Backend App Pod (Node 1)**: CSDL MySQL phục vụ backend.
* **MySQL for Keycloak Pod (Node 1)**: CSDL MySQL riêng cho dịch vụ Keycloak.

## 7️⃣ Storage Layer (Persistent Volumes Layer)

* **Persistent Volume (PV)**: Vùng lưu trữ vật lý được cấp phát cố định trên node.
* **Persistent Volume Claim (PVC)**: Yêu cầu tài nguyên lưu trữ từ PV cho các pod sử dụng.

---

## 8️⃣ Operation & Maintenance / Management (Quản lý & Vận hành)

Bên phải sơ đồ là các thành phần hỗ trợ vận hành hệ thống:

* **Code version git control**: Quản lý mã nguồn với Git.
* **CI/CD with GitAction & ArgoCD**: Tự động hóa triển khai liên tục (CI/CD) cho hệ thống.
* **Google K8s Cloud Service**: Dịch vụ Kubernetes trên nền tảng Google Cloud.
* **Log visualization**: Hệ thống quan sát và trực quan hóa log để theo dõi hoạt động hệ thống.

---

## 🔑 Đặc Điểm Chính

| Thành phần      | Loại             | Mức Độ Public | Ghi Chú                                     |
| --------------- | ---------------- | ------------- | ------------------------------------------- |
| Cloudflare      | External (Edge)  | Công khai     | Proxy & bảo mật truy cập từ bên ngoài       |
| Cloudflared Pod | Tunnel Connector | Pod nội bộ    | Kết nối Cloudflare tới các service nội bộ   |
| Frontend        | Deployment + SVC | ClusterIP     | Chỉ truy cập nội bộ qua Tunnel              |
| Backend         | Deployment + SVC | ClusterIP     | API nội bộ                                  |
| Keycloak        | Deployment + SVC | ClusterIP     | IAM service (Xác thực & Phân quyền)         |
| MySQL PV/PVC    | Lưu trữ          | Node 1        | Lưu dữ liệu bền vững cho Backend & Keycloak |

---

## ⚙️ Ưu Điểm Hiện Tại

* Kết nối bảo mật ra ngoài qua Cloudflare Tunnel.
* Phân tách rõ ràng giữa các dịch vụ (Frontend, Backend, IAM).
* Lưu trữ dữ liệu bền vững qua Persistent Volume.

---

## 🟡 Hạn Chế Hiện Tại

* Các dịch vụ chỉ expose dưới dạng **ClusterIP** — phụ thuộc hoàn toàn vào Cloudflare Tunnel để public.
* Chưa có **Ingress Controller** để quản lý route HTTP.
* Chưa có autoscaling hay High Availability (Backend & DB tập trung Node 1).
* Chưa có giải pháp giám sát/quan sát tập trung (Monitoring/Logging).
* Storage cố định tại Node 1 (rủi ro Single Point of Failure).

---

## ✅ Kiến Nghị Nâng Cấp

* Thêm **Ingress Controller** (ví dụ: NGINX Ingress) để dễ dàng quản lý traffic.
* Kích hoạt **HPA / Cluster Autoscaler** để mở rộng tự động.
* Áp dụng **Service Mesh** (Istio, Linkerd) cho observability & bảo mật nội bộ.
* Cân nhắc chuyển DB sang **Cloud SQL / Database Managed**.
* Xây dựng hệ thống **giám sát & logging tập trung** (Prometheus, Grafana, Loki).
* Bảo vệ traffic nội bộ bằng **Network Policies**.
* Sử dụng **ArgoCD hoặc FluxCD** cho GitOps CI/CD triển khai tự động.

---

## 🏁 Kết Luận

Kiến trúc hiện tại phù hợp cho các hệ thống nhỏ đến trung bình với bảo mật Cloudflare Tunnel. Để hướng tới môi trường production với khả năng mở rộng, an toàn và dễ giám sát hơn.

---

