FROM node:20-alpine AS builder
WORKDIR /app

RUN corepack enable

ARG VITE_BACKEND_URL
ENV VITE_BACKEND_URL=$VITE_BACKEND_URL

COPY package.json pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile
COPY . ./
RUN pnpm build

FROM node:20-alpine AS runner
WORKDIR /app
RUN npm install -g serve
COPY --from=builder /app/dist ./dist
EXPOSE 5173
CMD ["serve", "-s", "dist", "-l", "5173"]