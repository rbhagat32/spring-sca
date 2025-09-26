const isAdmin = (user: IUser) => {
  return user?.roles.includes("ROLE_ADMIN");
};

export { isAdmin };
